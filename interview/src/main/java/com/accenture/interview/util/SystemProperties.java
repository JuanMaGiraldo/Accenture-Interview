package com.accenture.interview.util;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:config.properties")
public class SystemProperties {

	@Value("${UPDATE_HOUR_TIME_LIMIT}")
	private Integer updateHourTimeLimit;

	@Value("${DELETE_HOUR_TIME_LIMIT}")
	private Integer deleteHourTimeLimit;

	@Value("${DELIVERY_PRICE}")
	private BigDecimal deliveryPrice;
	
	@Value("${IVA_PERCENTAGE}")
	private BigDecimal ivaPercentage;

	public Integer getUpdateHourTimeLimit() {
		return updateHourTimeLimit;
	}

	public void setUpdateHourTimeLimit(Integer updateHourTimeLimit) {
		this.updateHourTimeLimit = updateHourTimeLimit;
	}

	public Integer getDeleteHourTimeLimit() {
		return deleteHourTimeLimit;
	}

	public void setDeleteHourTimeLimit(Integer deleteHourTimeLimit) {
		this.deleteHourTimeLimit = deleteHourTimeLimit;
	}

	public BigDecimal getDeliveryPrice() {
		return deliveryPrice;
	}

	public void setDeliveryPrice(BigDecimal deliveryPrice) {
		this.deliveryPrice = deliveryPrice;
	}

	public BigDecimal getIvaPercentage() {
		return ivaPercentage;
	}

	public void setIvaPercentage(BigDecimal ivaPercentage) {
		this.ivaPercentage = ivaPercentage;
	}

	
}