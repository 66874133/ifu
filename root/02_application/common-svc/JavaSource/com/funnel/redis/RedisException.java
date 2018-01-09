package com.funnel.redis;

public class RedisException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String code;
	private String desc;

	/**
	 * 根据错误信息创建<br>
	 * 
	 * @param desc
	 *            错误信息
	 */
	public RedisException(String desc) {
		this.desc = desc;
	}

	/**
	 * 根据错误信息创建<br>
	 * 
	 * @param desc
	 *            错误信息
	 */
	public RedisException(String code, String desc) {
		this.desc = desc;
		this.code = code;
	}

	/**
	 * 根据错误信息和引起错误的异常对象创建<br>
	 * 
	 * @param desc
	 *            错误信息
	 * @param e
	 *            引起错误的异常对象
	 */
	public RedisException(String code, String desc, Exception e) {
		super(e);
		this.desc = desc;
		this.code = code;
	}

	/**
	 * 根据错误信息和引起错误的异常对象创建<br>
	 * 
	 * @param desc
	 *            错误信息
	 * @param e
	 *            引起错误的异常对象
	 */
	public RedisException(String desc, Exception e) {
		super(e);
		this.desc = desc;
	}

	@Override
	public String getMessage() {
		return desc
				+ (super.getCause() != null ? ":"
						+ super.getCause().getMessage() : "");
	}

	/**
	 * 获取错误信息<br>
	 * 
	 * @return 错误信息
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * 设置错误信息<br>
	 * 
	 * @param desc
	 *            错误信息
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}