package com.st.framework;

import java.util.ArrayList;
import java.util.List;

/**
 * @see 分页类
 * @author MRCHEN
 * @category 只需传入页码 和 每页大小 即可 ,非法页码已做处理
 * @param <T>
 *            需要分页的实体类
 */
public class Pagination<T> {
	/**
	 * @see 每页记录数
	 */
	private long pageSize = 10;
	/**
	 * @see 当前页码
	 */
	private long pageIndex = 1;
	/**
	 * @see 查询对象总记录条数
	 */
	private long recondSum;
	
	@SuppressWarnings("unused")
	private long nextPage;
	
	@SuppressWarnings("unused")
	private long prePage;
	/**
	 * @see 总页数
	 */
	private long pageSum;


	/**
	 * @see 是否有上一页
	 */
	@SuppressWarnings("unused")
	private boolean isHasPageUp;
	/**
	 * @see 是否有下一页
	 */
	private boolean isHasPageDown;
	
	public long getNextPage() {
		
		if(isHasPageDown){
			return pageIndex+1;
		}
		return 0l;
	}

	public void setNextPage(long nextPage) {
		this.nextPage = nextPage;
	}

	public long getPrePage() {
		if(isHasPageDown){
			return pageIndex-1;
		}
		return 0l;
	}

	public void setPrePage(long prePage) {
		this.prePage = prePage;
	}
	/**
	 * @see 查询对象返回的记录
	 */
	private List<T> results = new ArrayList<T>();

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}

	public boolean getIsHasPageUp() {
		return this.pageIndex > 1;
	}

	public boolean getIsHasPageDown() {
		return this.pageIndex < this.pageSum;
	}

	public long getPageSum() {
		return pageSum;
	}

	public long getRecondSum() {
		return recondSum;
	}

	public void setRecondSum(long recondSum) {
		this.recondSum = recondSum;

		long pageSum = this.recondSum / this.pageSize;
		long mod = this.recondSum % this.pageSize;
		if (mod != 0) {
			pageSum = pageSum + 1;
		}

		if (this.pageIndex >= pageSum) {
			this.pageIndex = pageSum;
		}
		this.pageSum = pageSum;
	}

	public long getPageSize() {
		return pageSize;
	}

	public void setPageSize(long pageSize) {
		if (pageSize <= 0) {
			this.pageSize = 10;
		} else {
			this.pageSize = pageSize;
		}
	}

	public long getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(long pageIndex) {
		if (pageIndex <= 0) {
			this.pageIndex = 1;
		} else {
			this.pageIndex = pageIndex;
		}
	}

}
