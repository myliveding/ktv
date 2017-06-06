package com.st.core.util;

import com.st.javabean.PageModel;
import org.springframework.ui.Model;

import java.util.List;

public class GlobalMethodUtil {

	private static GlobalMethodUtil instance = new GlobalMethodUtil();

	private GlobalMethodUtil() {

	}

	public static GlobalMethodUtil getInstance() {
		return instance;
	}

	/**
	 * 
	 * 
	 * <ul>
	 * <li>
	 * <b>原因：<br/>
	 * <p>
	 * [2014-8-21]gaozhanglei<br/>
	 * 
	 * @param list
	 * @param pagesize
	 * @param pageNum
	 * @param model
	 * @return
	 *         </p>
	 *         </li>
	 *         </ul>
	 */
	@SuppressWarnings("rawtypes")
	public List getListForPage(Model model, List list, int pagesize, int pageNum) {

		PageModel pm = new PageModel(list, pagesize, pageNum);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("TotalPages", pm.getTotalPages());
		model.addAttribute("PreviousPage", pageNum - 1 > 0 ? pageNum - 1 : 0);
		model.addAttribute("NextPage", pm.isHasNextPage() == true ? pageNum + 1
				: 0);
		list = pm.getObjects(pageNum);
		return list;
	}
	
	
	

	

}
