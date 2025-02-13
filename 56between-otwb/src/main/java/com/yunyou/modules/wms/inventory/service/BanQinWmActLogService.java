package com.yunyou.modules.wms.inventory.service;

import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.common.utils.collection.ListUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.inventory.entity.BanQinWmActInOutEntity;
import com.yunyou.modules.wms.inventory.entity.BanQinWmActLog;
import com.yunyou.modules.wms.inventory.entity.BanQinWmActLogEntity;
import com.yunyou.modules.wms.inventory.mapper.BanQinWmActLogMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 库存交易日志Service
 * @author WMJ
 * @version 2020-04-14
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmActLogService extends CrudService<BanQinWmActLogMapper, BanQinWmActLog> {

	public BanQinWmActLog get(String id) {
		return super.get(id);
	}
	
	public List<BanQinWmActLog> findList(BanQinWmActLog wmActLog) {
		return mapper.findList(wmActLog);
	}

	@Transactional
	public void save(BanQinWmActLog wmActLog) {
		super.save(wmActLog);
	}

	@Transactional
	public void saveAll(List<BanQinWmActLog> list) {
		for (BanQinWmActLog log : list) {
			this.save(log);
		}
	}
	
	@Transactional
	public void delete(BanQinWmActLog wmActLog) {
		super.delete(wmActLog);
	}

	public List<BanQinWmActLog> findByLotNum(String lotNum, String orgId) {
		BanQinWmActLog log = new BanQinWmActLogEntity();
		log.setLotNum(lotNum);
		log.setOrgId(orgId);
		return this.findList(log);
	}

	public List<BanQinWmActLog> findByOrderNo(String orderNo, String orgId) {
		BanQinWmActLog log = new BanQinWmActLogEntity();
		log.setOrderNo(orderNo);
		log.setOrgId(orgId);
		return this.findList(log);
	}

	public List<BanQinWmActLogEntity> findByOpTime(Date beginTime, Date endTime, String orgId) {
		BanQinWmActLogEntity entity = new BanQinWmActLogEntity();
		entity.setBeginTime(beginTime);
		entity.setEndTime(endTime);
		entity.setOrgId(orgId);
		return mapper.findEntity(entity);
	}

	public List<BanQinWmActLogEntity> findLast(String orgId, Date opTime) {
		BanQinWmActLogEntity entity = new BanQinWmActLogEntity();
		entity.setOrgId(orgId);
		entity.setOpTime(opTime);
		return mapper.findLast(entity);
	}

	public List<BanQinWmActLogEntity> findAsnData(BanQinWmActLogEntity entity) {
		return mapper.findAsnData(entity);
	}

	public List<BanQinWmActLogEntity> findSoData(BanQinWmActLogEntity entity) {
		return mapper.findSoData(entity);
	}

	public List<BanQinWmActLogEntity> findAdData(BanQinWmActLogEntity entity) {
		return mapper.findAdData(entity);
	}

	public List<BanQinWmActLogEntity> findTfData(BanQinWmActLogEntity entity) {
		return mapper.findTfData(entity);
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

	public Page<BanQinWmActInOutEntity> findInOutData(Page page, BanQinWmActLogEntity entity) {
		List<BanQinWmActInOutEntity> result = ListUtil.newArrayList();
		entity.setPage(page);
		List<BanQinWmActLogEntity> list = mapper.findInOutData(entity);
		if (CollectionUtil.isNotEmpty(list)) {
			// 获取所有批次的期末库存
			entity.setOpTime(entity.getBeginTime());
			List<BanQinWmActLogEntity> last = mapper.findLast(entity);
			Map<Date, List<BanQinWmActLogEntity>> dateMap = list.stream().collect(Collectors.groupingBy(BanQinWmActLogEntity::getOpTime, LinkedHashMap::new, Collectors.toList()));
			int i = 0; BanQinWmActInOutEntity nextEntity = new BanQinWmActInOutEntity();
			for (Map.Entry<Date, List<BanQinWmActLogEntity>> entry : dateMap.entrySet()) {
				BanQinWmActInOutEntity inOutEntity = new BanQinWmActInOutEntity();
				// 计算年月日
				setDateInfo(entry.getKey(), inOutEntity);
				// 计算期初库存
				if (i == 0) {
					setInvBeforeInfo(last, inOutEntity);
				} else {
					inOutEntity.setPlBefore(nextEntity.getPlBefore());
					inOutEntity.setQtyBefore(nextEntity.getQtyBefore());
					inOutEntity.setGrossWeightBefore(nextEntity.getGrossWeightBefore());
				}
				// 计算本期进库
				setQtyInInfo(entry.getValue(), inOutEntity);
				// 计算本期出库
				setQtyOutInfo(entry.getValue(), inOutEntity);
				// 计算期末库存
				setInvAfterInfo(inOutEntity);
				result.add(inOutEntity);
				// 计算下一次的期初
				nextEntity.setPlBefore(inOutEntity.getPlAfter());
				nextEntity.setQtyBefore(inOutEntity.getQtyAfter());
				nextEntity.setGrossWeightBefore(inOutEntity.getGrossWeightAfter());
				i++;
			}
		}

		page.setList(result);
		return page;
	}

	private void setDateInfo(Date date, BanQinWmActInOutEntity inOutEntity) {
		String[] dateArray = DateUtils.formatDate(date).split("-");
		inOutEntity.setYear(dateArray[0]);
		inOutEntity.setMonth(dateArray[1]);
		inOutEntity.setDays(dateArray[2]);
	}

	private void setInvBeforeInfo(List<BanQinWmActLogEntity> list, BanQinWmActInOutEntity inOutEntity) {
		BigDecimal sumQty = new BigDecimal("0");
		BigDecimal sumPlQty = new BigDecimal("0");
		BigDecimal sumWeight = new BigDecimal("0");
		for (BanQinWmActLogEntity entity : list) {
			sumQty = sumQty.add(BigDecimal.valueOf(entity.getQtyEaAfter()));
			sumPlQty = sumPlQty.add(BigDecimal.valueOf(entity.getQtyEaAfter()).divide(BigDecimal.valueOf(entity.getPlQty()), 2, BigDecimal.ROUND_DOWN));
			if (StringUtils.isNotBlank(entity.getLotAtt05()) && isNumeric(entity.getLotAtt05())) {
				sumWeight = sumWeight.add(BigDecimal.valueOf(entity.getQtyEaAfter()).multiply(new BigDecimal(entity.getLotAtt05())));
			}
		}

		inOutEntity.setPlBefore(sumPlQty);
		inOutEntity.setQtyBefore(sumQty);
		inOutEntity.setGrossWeightBefore(sumWeight);
	}

	private void setQtyInInfo(List<BanQinWmActLogEntity> list, BanQinWmActInOutEntity inOutEntity) {
		BigDecimal sumQty = new BigDecimal("0");
		BigDecimal sumPlQty = new BigDecimal("0");
		BigDecimal sumWeight = new BigDecimal("0");
		// 获取入库记录
		list = list.stream().filter(s -> "1".equals(s.getTranType())).collect(Collectors.toList());
		for (BanQinWmActLogEntity entity : list) {
			sumQty = sumQty.add(BigDecimal.valueOf(entity.getQtyEaOp()));
			sumPlQty = sumPlQty.add(BigDecimal.valueOf(entity.getQtyEaOp()).divide(BigDecimal.valueOf(entity.getPlQty()), 2, BigDecimal.ROUND_DOWN));
			if (StringUtils.isNotBlank(entity.getLotAtt05()) && isNumeric(entity.getLotAtt05())) {
				sumWeight = sumWeight.add(BigDecimal.valueOf(entity.getQtyEaOp()).multiply(new BigDecimal(entity.getLotAtt05())));
			}
		}

		inOutEntity.setPlIn(sumPlQty);
		inOutEntity.setQtyIn(sumQty);
		inOutEntity.setGrossWeightIn(sumWeight);
	}

	private void setQtyOutInfo(List<BanQinWmActLogEntity> list, BanQinWmActInOutEntity inOutEntity) {
		BigDecimal sumQty = new BigDecimal("0");
		BigDecimal sumPlQty = new BigDecimal("0");
		BigDecimal sumWeight = new BigDecimal("0");
		// 获取出库记录
		list = list.stream().filter(s -> "0".equals(s.getTranType())).collect(Collectors.toList());
		for (BanQinWmActLogEntity entity : list) {
			sumQty = sumQty.add(BigDecimal.valueOf(Math.abs(entity.getQtyEaOp())));
			sumPlQty = sumPlQty.add(BigDecimal.valueOf(Math.abs(entity.getQtyEaOp())).divide(BigDecimal.valueOf(entity.getPlQty()), 2, BigDecimal.ROUND_DOWN));
			if (StringUtils.isNotBlank(entity.getLotAtt05()) && isNumeric(entity.getLotAtt05())) {
				sumWeight = sumWeight.add(BigDecimal.valueOf(Math.abs(entity.getQtyEaOp())).multiply(new BigDecimal(entity.getLotAtt05())));
			}
		}

		inOutEntity.setPlOut(sumPlQty);
		inOutEntity.setQtyOut(sumQty);
		inOutEntity.setGrossWeightOut(sumWeight);
	}

	private void setInvAfterInfo(BanQinWmActInOutEntity inOutEntity) {
		inOutEntity.setPlAfter(inOutEntity.getPlBefore().add(inOutEntity.getPlIn().subtract(inOutEntity.getPlOut())));
		inOutEntity.setQtyAfter(inOutEntity.getQtyBefore().add(inOutEntity.getQtyIn().subtract(inOutEntity.getQtyOut())));
		inOutEntity.setGrossWeightAfter(inOutEntity.getGrossWeightBefore().add(inOutEntity.getGrossWeightIn().subtract(inOutEntity.getGrossWeightOut())));
	}

}