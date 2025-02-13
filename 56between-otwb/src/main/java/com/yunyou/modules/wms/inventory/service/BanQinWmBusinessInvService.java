package com.yunyou.modules.wms.inventory.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.common.utils.collection.ListUtil;
import com.yunyou.common.utils.time.DateUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.inventory.entity.BanQinWmActLogEntity;
import com.yunyou.modules.wms.inventory.entity.BanQinWmBusinessInv;
import com.yunyou.modules.wms.inventory.entity.BanQinWmBusinessInvEntity;
import com.yunyou.modules.wms.inventory.mapper.BanQinWmBusinessInvMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 业务库存Service
 * @author WMJ
 * @version 2020-04-26
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmBusinessInvService extends CrudService<BanQinWmBusinessInvMapper, BanQinWmBusinessInv> {
	@Autowired
	private BanQinWmActLogService wmActLogService;
	@Autowired
	private BanQinInvActLogCreateService invActLogCreateService;

	public BanQinWmBusinessInv get(String id) {
		return super.get(id);
	}
	
	public List<BanQinWmBusinessInv> findList(BanQinWmBusinessInv inv) {
		return mapper.findList(inv);
	}

	@Transactional
	public void save(BanQinWmBusinessInv inv) {
		super.save(inv);
	}

	@Transactional
	public void saveAll(List<BanQinWmBusinessInv> list) {
		for (BanQinWmBusinessInv inv : list) {
			this.save(inv);
		}
	}
	
	@Transactional
	public void delete(BanQinWmBusinessInv inv) {
		super.delete(inv);
	}

	public Page<BanQinWmBusinessInvEntity> findPage(Page page, BanQinWmBusinessInvEntity entity) {
		entity.setPage(page);
		page.setList(mapper.findPage(entity));
		return page;
	}

	public List<BanQinWmBusinessInv> findByLot(String lot, String orgId) {
		BanQinWmBusinessInv condition = new BanQinWmBusinessInv();
		condition.setLot(lot);
		condition.setOrgId(orgId);
		return this.findList(condition);
	}

	public BanQinWmBusinessInvEntity count(BanQinWmBusinessInvEntity entity) {
		BanQinWmBusinessInvEntity result = new BanQinWmBusinessInvEntity();
		List<BanQinWmBusinessInvEntity> list = mapper.findPage(entity);
		BigDecimal totalBgQty = new BigDecimal("0");
		BigDecimal totalInQty = new BigDecimal("0");
		BigDecimal totalOutQty = new BigDecimal("0");
		BigDecimal totalEdQty = new BigDecimal("0");
		for (BanQinWmBusinessInvEntity inv : list) {
			switch (inv.getOrderType()) {
				case "BG":
					totalBgQty = totalBgQty.add(BigDecimal.valueOf(inv.getQtyEaBefore()));
					break;
				case "ASN":
					totalInQty = totalInQty.add(BigDecimal.valueOf(inv.getQtyEaOp()));
					break;
				case "SO":
					totalOutQty = totalOutQty.add(BigDecimal.valueOf(Math.abs(inv.getQtyEaOp())));
					break;
				case "AD":
				case "TF":
					if (inv.getTranType().equals("1")) {
						totalInQty = totalInQty.add(BigDecimal.valueOf(inv.getQtyEaOp()));
					} else if (inv.getTranType().equals("0")) {
						totalOutQty = totalOutQty.add(BigDecimal.valueOf(Math.abs(inv.getQtyEaOp())));
					}
					break;
				case "ED":
					totalEdQty = totalEdQty.add(BigDecimal.valueOf(inv.getQtyEaAfter()));
					break;
			}
		}
		result.setTotalBgQty(totalBgQty);
		result.setTotalInQty(totalInQty);
		result.setTotalOutQty(totalOutQty);
		result.setTotalEdQty(totalEdQty);

		return result;
	}

	@Transactional
	public ResultMessage settle(String settleMonth, String orgId) {
		ResultMessage msg = new ResultMessage();
		Date beginDate = DateUtil.beginOfMonth(DateUtils.parseDate(settleMonth));
		Date endDate = DateUtil.endOfMonth(DateUtils.parseDate(settleMonth));
		// 先删除之前的
		this.deleteAll(findByLot(settleMonth, orgId));
		List<BanQinWmActLogEntity> list = wmActLogService.findByOpTime(beginDate, endDate, orgId);
		// 获取<开始时间的所有批次的最后一条
		List<BanQinWmActLogEntity> lastLotList = getAllLotByBeginTime(beginDate, orgId, list);
		list.addAll(lastLotList);
		// 计算报表所需字段
		List<BanQinWmActLogEntity> result = ListUtil.newArrayList();
		Map<String, List<BanQinWmActLogEntity>> lotMap = list.stream().collect(Collectors.groupingBy(BanQinWmActLogEntity::getLotNum, LinkedHashMap::new, Collectors.toList()));
		Map<String, List<BanQinWmActLogEntity>> tfMap = list.stream().filter(s -> "TF".equals(s.getOrderType())).collect(Collectors.groupingBy(s -> s.getOrderNo() + "@" + s.getLineNo()));
		lotMap.values().forEach(v -> {
			int seq = 1;
			for (BanQinWmActLogEntity logEntity : v) {
				logEntity.setInboundDate(getBeforeDate(logEntity.getLotAtt03(), beginDate));
				logEntity.setBeginTime(beginDate);
				logEntity.setEndTime(endDate);
				logEntity.setSeq(seq);
				seq++;
				switch (logEntity.getOrderType()) {
					case "ASN":
						setAsnInfo(logEntity);
						break;
					case "SO":
						setSoInfo(logEntity);
						break;
					case "AD":
						setAdInfo(logEntity);
						break;
					case "TF":
						setTfInfo(logEntity, tfMap);
						break;
					case "ED":
						setEdInfo(logEntity, endDate);
						break;
				}
			}
			// 插入期初期末
			insertBgAndEd(v, beginDate, endDate);
			result.addAll(v);
		});

		for (BanQinWmActLogEntity entity : result) {
			insertBusinessInv(entity, settleMonth);
		}

		msg.setSuccess(true);
		msg.setMessage("操作成功");
		return msg;
	}

	@Transactional
	public void insertBusinessInv(BanQinWmActLogEntity logEntity, String settleMonth) {
		// 插入到业务库存
		BanQinWmBusinessInv inv = new BanQinWmBusinessInv();
		BeanUtils.copyProperties(logEntity, inv);
		inv.setId(IdGen.uuid());
		inv.setIsNewRecord(true);
		inv.setBgDate(logEntity.getInboundDate());
		inv.setStoreDays(logEntity.getDays());
		inv.setLot(settleMonth);
		this.save(inv);
	}

	private List<BanQinWmActLogEntity> getAllLotByBeginTime(Date beginDate, String orgId, List<BanQinWmActLogEntity> list) {
		List<BanQinWmActLogEntity> result = ListUtil.newArrayList();
		// 获取所有批次的期末库存
		List<BanQinWmActLogEntity> last = wmActLogService.findLast(orgId, beginDate);
		// 剔除本期存在的批次
		if (CollectionUtil.isNotEmpty(last)) {
			List<String> hasLotList = list.stream().map(BanQinWmActLogEntity::getLotNum).distinct().collect(Collectors.toList());
			for (BanQinWmActLogEntity log : last) {
				if (!hasLotList.contains(log.getLotNum())) {
					log.setTranType("2");
					log.setOrderNo(null);
					log.setLineNo(null);
					log.setOrderType("BG");
					log.setFirstOwner(null);
					log.setToOwner(null);
					log.setOpTime(null);
					log.setTfDate(null);
					log.setAdDate(null);
					log.setInboundDate(null);
					log.setOutboundDate(null);
					log.setQtyOut(null);
					log.setWeightOut(null);
					log.setQtyEaOp(null);
					log.setQtyEaBefore(log.getQtyEaAfter());
					log.setQtyEaAfter(null);
					if (StringUtils.isNotBlank(log.getLotAtt05()) && isNumeric(log.getLotAtt05())) {
						BigDecimal weight = new BigDecimal(log.getLotAtt05());
						log.setWeightInv(BigDecimal.valueOf(log.getQtyEaBefore()).multiply(weight));
					}
					result.add(log);
					// 添加期末
					BanQinWmActLogEntity newLog = new BanQinWmActLogEntity();
					BeanUtils.copyProperties(log, newLog);
					newLog.setQtyEaBefore(null);
					newLog.setQtyEaAfter(log.getQtyEaBefore());
					newLog.setOrderType("ED");
					result.add(newLog);
				}
			}
		}

		return result;
	}

	private void insertBgAndEd(List<BanQinWmActLogEntity> list, Date beginDate, Date endDate) {
		// 判断当前批次记录数
		BanQinWmActLogEntity first = list.get(0);
		// 当前是否是期初
		if (!first.getOrderType().equals("BG")) {
			list.add(0, addBeginLog(first, beginDate));
		}
		// 最后一条
		BanQinWmActLogEntity end = list.get(list.size() - 1);
		// 当前是否是期末
		if (!end.getOrderType().equals("ED")) {
			list.add(addEndLog(end, beginDate, endDate));
		}
	}

	private BanQinWmActLogEntity addBeginLog(BanQinWmActLogEntity entity, Date beginDate) {
		BanQinWmActLogEntity result = new BanQinWmActLogEntity();
		BeanUtils.copyProperties(entity, result);
		result.setTranType("2");
		result.setOrderNo(null);
		result.setLineNo(null);
		result.setOrderType("BG");
		result.setFirstOwner(null);
		result.setToOwner(null);
		result.setTfDate(null);
		result.setAdDate(null);
		result.setInboundDate(getBeforeDate(entity.getLotAtt03(), beginDate));
		result.setOutboundDate(null);
		result.setOpTime(null);
		result.setQtyIn(null);
		result.setWeightIn(null);
		result.setQtyOut(null);
		result.setWeightOut(null);
		result.setQtyEaOp(null);
		result.setQtyEaBefore(entity.getQtyEaBefore());
		result.setQtyEaAfter(null);
		result.setSeq(0);
		if (StringUtils.isNotBlank(entity.getLotAtt05()) && isNumeric(entity.getLotAtt05())) {
			BigDecimal weight = new BigDecimal(entity.getLotAtt05());
			result.setWeightInv(BigDecimal.valueOf(entity.getQtyEaBefore()).multiply(weight));
		}
		result.setDays(null);
		return result;
	}

	private BanQinWmActLogEntity addEndLog(BanQinWmActLogEntity entity, Date beginDate, Date endDate) {
		BanQinWmActLogEntity result = new BanQinWmActLogEntity();
		BeanUtils.copyProperties(entity, result);
		result.setTranType("2");
		result.setOrderNo(null);
		result.setLineNo(null);
		result.setOrderType("ED");
		result.setFirstOwner(null);
		result.setToOwner(null);
		result.setTfDate(null);
		result.setAdDate(null);
		result.setInboundDate(getBeforeDate(entity.getLotAtt03(), beginDate));
		result.setOutboundDate(null);
		result.setOpTime(null);
		result.setQtyIn(null);
		result.setWeightIn(null);
		result.setQtyOut(null);
		result.setWeightOut(null);
		result.setQtyEaOp(null);
		result.setQtyEaBefore(null);
		result.setQtyEaAfter(entity.getQtyEaAfter());
		result.setSeq(result.getSeq() + 1);
		if (StringUtils.isNotBlank(entity.getLotAtt05()) && isNumeric(entity.getLotAtt05())) {
			BigDecimal weight = new BigDecimal(entity.getLotAtt05());
			result.setWeightInv(BigDecimal.valueOf(entity.getQtyEaAfter()).multiply(weight));
		}
		if (null != result.getInboundDate()) {
			result.setDays(Period.between(date2LocalDate(entity.getInboundDate()), date2LocalDate(endDate)).getDays());
		} else {
			result.setDays(null);
		}

		return result;
	}

	private void setAsnInfo(BanQinWmActLogEntity logEntity) {
		logEntity.setQtyIn(BigDecimal.valueOf(logEntity.getQtyEaOp()));
		setInLog(logEntity);
	}

	private void setSoInfo(BanQinWmActLogEntity logEntity) {
		logEntity.setQtyOut(BigDecimal.valueOf(Math.abs(logEntity.getQtyEaOp())));
		setOutLog(logEntity);
		logEntity.setOutboundDate(logEntity.getOpTime());
		if (null != logEntity.getInboundDate()) {
			logEntity.setDays(Period.between(date2LocalDate(logEntity.getInboundDate()), date2LocalDate(logEntity.getOutboundDate())).getDays());
		}
	}

	private void setAdInfo(BanQinWmActLogEntity logEntity) {
		if (logEntity.getQtyEaOp() > 0) {
			logEntity.setQtyIn(BigDecimal.valueOf(logEntity.getQtyEaOp()));
			setInLog(logEntity);
		} else {
			logEntity.setQtyOut(BigDecimal.valueOf(Math.abs(logEntity.getQtyEaOp())));
			setOutLog(logEntity);
		}
		logEntity.setAdDate(logEntity.getOpTime());
	}

	private void setTfInfo(BanQinWmActLogEntity logEntity, Map<String, List<BanQinWmActLogEntity>> tfMap) {
		List<BanQinWmActLogEntity> tfList = tfMap.get(logEntity.getOrderNo() + "@" + logEntity.getLineNo());
		if (logEntity.getQtyEaOp() > 0) {
			Optional<BanQinWmActLogEntity> first = tfList.stream().filter(s -> s.getQtyEaOp() < 0).findFirst();
			logEntity.setQtyIn(BigDecimal.valueOf(logEntity.getQtyEaOp()));
			first.ifPresent(f -> logEntity.setFirstOwner(f.getOwnerName()));
			logEntity.setToOwner(logEntity.getOwnerName());
			setInLog(logEntity);
		} else {
			Optional<BanQinWmActLogEntity> first = tfList.stream().filter(s -> s.getQtyEaOp() > 0).findFirst();
			logEntity.setQtyOut(BigDecimal.valueOf(Math.abs(logEntity.getQtyEaOp())));
			logEntity.setFirstOwner(logEntity.getOwnerName());
			first.ifPresent(f -> logEntity.setToOwner(f.getOwnerName()));
			setOutLog(logEntity);
		}
		logEntity.setTfDate(logEntity.getOpTime());
	}

	private void setEdInfo(BanQinWmActLogEntity logEntity, Date endDate) {
		if (null != logEntity.getInboundDate()) {
			logEntity.setDays(Period.between(date2LocalDate(logEntity.getInboundDate()), date2LocalDate(endDate)).getDays());
		}
	}

	private LocalDate date2LocalDate(Date date) {
		if (null == date) {
			return null;
		}
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	private Date getBeforeDate(Date inBoundDate, Date beginDate) {
		if (null == inBoundDate) return null;
		return inBoundDate.after(beginDate) ? inBoundDate : beginDate;
	}

	private void setInLog(BanQinWmActLogEntity logEntity) {
		if (StringUtils.isNotBlank(logEntity.getLotAtt05()) && isNumeric(logEntity.getLotAtt05())) {
			BigDecimal weight = new BigDecimal(logEntity.getLotAtt05());
			logEntity.setWeightIn(BigDecimal.valueOf(logEntity.getQtyEaOp()).multiply(weight));
			logEntity.setWeightInv(BigDecimal.valueOf(logEntity.getQtyEaAfter()).multiply(weight));
		}
	}

	private void setOutLog(BanQinWmActLogEntity logEntity) {
		if (StringUtils.isNotBlank(logEntity.getLotAtt05()) && isNumeric(logEntity.getLotAtt05())) {
			BigDecimal weight = new BigDecimal(logEntity.getLotAtt05());
			logEntity.setWeightOut(BigDecimal.valueOf(Math.abs(logEntity.getQtyEaOp())).multiply(weight));
			logEntity.setWeightInv(BigDecimal.valueOf(logEntity.getQtyEaAfter()).multiply(weight));
		}
	}

	private boolean isNumeric(String str) {
		// 该正则表达式可以匹配所有的数字 包括负数
		Pattern pattern = Pattern.compile("-?[0-9]+\\.?[0-9]*");
		String bigStr;
		try {
			bigStr = new BigDecimal(str).toString();
		} catch (Exception e) {
			return false;
		}

		Matcher isNum = pattern.matcher(bigStr);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	@Transactional
	public ResultMessage reCalcAndSettle(BanQinWmBusinessInvEntity entity) {
		ResultMessage msg = new ResultMessage();
		if (!entity.getOrderTimeTo().after(entity.getOrderTimeFm())) {
			msg.setSuccess(false);
			msg.setMessage("订单时间到必须大于订单时间从!");
			return msg;
		}

		// 重新计算
		invActLogCreateService.createActLog(entity.getOrderTimeFm(), entity.getOrderTimeTo(), entity.getOrgId(), true);
		// 重新结算
		Date date = DateUtil.beginOfMonth(entity.getOrderTimeFm());
		Date curDate = DateUtil.beginOfMonth(new Date());
		while (date.before(curDate) || date.equals(curDate)) {
			settle(DateUtils.formatDate(date, "YYYY-MM"), entity.getOrgId());
			date = DateUtil.addMonths(date, 1);
		}

		msg.setSuccess(true);
		msg.setMessage("操作成功");
		return msg;
	}

}