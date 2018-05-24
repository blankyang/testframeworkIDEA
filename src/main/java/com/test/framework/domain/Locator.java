package com.test.framework.domain;

public class Locator {
	// 元素by中的表达式
	private String elementExp;
	// 等待元素时间
	private int waitSec;

	public enum ByType {
		xpath, id, linkText, name, className, cssSelector, partialLinkText, tagName
	}

	private ByType byType;

	public Locator(String elementExp, int waitSec, ByType byType) {
		this.waitSec = waitSec;
		this.elementExp = elementExp;
		this.byType = byType;
	}

	public Locator() {

	}

	public Locator(String elementExp) {
		this.elementExp = elementExp;
		this.waitSec = 5;
	}

	public String getElementExp() {
		return elementExp;
	}

	public void setElementExp(String elementExp) {
		this.elementExp = elementExp;
	}

	public int getWaitSec() {
		return waitSec;
	}

	public void setWaitSec(int waitSec) {
		this.waitSec = waitSec;
	}

	public ByType getByType() {
		return byType;
	}

	public void setByType(ByType byType) {
		this.byType = byType;
	}

	public Locator(String elementExp, int waitSec) {
		super();
		this.elementExp = elementExp;
		this.waitSec = waitSec;
	}

}
