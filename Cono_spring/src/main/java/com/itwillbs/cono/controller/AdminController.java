package com.itwillbs.cono.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwillbs.cono.service.AdminService;
import com.itwillbs.cono.vo.AdminNoticeDTO;
import com.itwillbs.cono.vo.PageInfo;

@Controller
public class AdminController {

	@Autowired
	AdminService service;

	// -------------- 관리자 메인 이동 (관리자) - 김도은 -------------
	@RequestMapping(value = "admin_center_main", method = RequestMethod.GET)
	public String adminCenter() {
		return "/admin_center/main";
	}
	// -------------- 고객센터 공지사항 리스트, 검색기능 (관리자) - 김도은 -------------

	// => 파라미터로 전달되는 pageNum 파라미터가 없을 경우를 대비하여 기본값 0 으로 지정
	@RequestMapping(value = "AdminNoticeList", method = RequestMethod.GET)
	public String list(@RequestParam(defaultValue = "") String searchType,
			@RequestParam(defaultValue = "") String search, @RequestParam(defaultValue = "1") int pageNum,
			Model model) {

		int listCount = service.getNoticeListCount("searchType", "search");
		int listLimit = 10; // 한 페이지 당 표시할 게시물 목록 갯수
		int pageLimit = 10; // 한 페이지 당 표시할 페이지 목록 갯수

		// 페이징 처리를 위한 계산 작업
		int maxPage = (int) Math.ceil((double) listCount / listLimit);
		int startPage = ((int) ((double) pageNum / pageLimit + 0.9) - 1) * pageLimit + 1;
		int endPage = startPage + pageLimit - 1;
		if (endPage > maxPage) {
			endPage = maxPage;
		}

		int startRow = (pageNum - 1) * listLimit;

		PageInfo pageInfo = new PageInfo();
		pageInfo.setPageNum(pageNum);
		pageInfo.setMaxPage(maxPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);
		pageInfo.setListCount(listCount);
		pageInfo.setStartRow(startRow);
		pageInfo.setListLimit(listLimit);

		List<AdminNoticeDTO> noticeList = service.getNoticeList(searchType, "%" + search + "%", pageInfo);

		model.addAttribute("noticeList", noticeList);
		model.addAttribute("pageInfo", pageInfo);

		return "userCenter/admin_notice_list";
	}

	// 고객센터 공지사항 리스트 - POST
	@RequestMapping(value = "AdminNoticeList", method = RequestMethod.POST)
	public String noticeList(@RequestParam String searchType, @RequestParam String search,
			@RequestParam(defaultValue = "1") int pageNum, Model model) {

		int listCount = service.getNoticeListCount(searchType, "%" + search + "%");

		int listLimit = 10; // 한 페이지 당 표시할 게시물 목록 갯수
		int pageLimit = 10; // 한 페이지 당 표시할 페이지 목록 갯수

		int maxPage = (int) Math.ceil((double) listCount / listLimit);
		int startPage = ((int) ((double) pageNum / pageLimit + 0.9) - 1) * pageLimit + 1;
		int endPage = startPage + pageLimit - 1;
		if (endPage > maxPage) {
			endPage = maxPage;
		}

		int startRow = (pageNum - 1) * listLimit;

		PageInfo pageInfo = new PageInfo();
		pageInfo.setPageNum(pageNum);
		pageInfo.setMaxPage(maxPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);
		pageInfo.setListCount(listCount);
		pageInfo.setStartRow(startRow);
		pageInfo.setListLimit(listLimit);
		List<AdminNoticeDTO> noticeList = service.getNoticeList(searchType, "%" + search + "%", pageInfo);

		model.addAttribute("noticeList", noticeList);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("searchType", searchType);
		model.addAttribute("search", search);

		return "userCenter/admin_notice_list";
	}

	// -------------- 고객센터 공지사항 글쓰기 (관리자) - 김도은 -------------
	// 글쓰기 폼 - GET
	@RequestMapping(value = "AdminNoticeWrite.admin", method = RequestMethod.GET)
	public String write() {
		return "userCenter/admin_notice_write";
	}

	// 글쓰기 비즈니스 로직 - POST
	@RequestMapping(value = "AdminNoticeWrite.admin", method = RequestMethod.POST)
	public String writePost(@ModelAttribute AdminNoticeDTO noticeList, Model model) {
		int insertCount = service.writeNotice(noticeList);

		if (insertCount == 0) {
			model.addAttribute("msg", "글 등록 실패!");
			return "fail_back";
		}
		return "redirect:/AdminNoticeList";
	}

	// 글쓰기 상세페이지 - GET
	@RequestMapping(value = "AdminNoticeView.admin", method = RequestMethod.GET)
	public String adminNoticeView(@RequestParam String notice_idx, Model model) {
		AdminNoticeDTO noticeList = service.getAdminNoticeView(notice_idx);

		model.addAttribute("noticeList", noticeList);

		return "userCenter/admin_notice_view";
	}

	// 글 삭제 비즈니스 로직 - POST
	@RequestMapping(value = "AdminNoticeDeletePro.admin", method = RequestMethod.GET)
	public String deleteNotice(@ModelAttribute AdminNoticeDTO noticeList, @RequestParam int pageNum, Model model) {
		boolean isDeleteSuccess = service.removeNotice(noticeList, pageNum);

		if (!isDeleteSuccess) {
			model.addAttribute("msg", "삭제실패!");
			return "fail_back";
		}
		model.addAttribute("pageNum", pageNum);

		return "redirect:/AdminNoticeList";
	}

	// 글 수정 폼 - GET
	@RequestMapping(value = "AdminNoticeModifyForm.admin", method = RequestMethod.GET)
	public String modifyNotice(@RequestParam String notice_idx, Model model) {
		AdminNoticeDTO noticeList = service.getAdminNoticeView(notice_idx);

		model.addAttribute("noticeList", noticeList);

		return "userCenter/admin_notice_modify";
	}

	// 글 수정 비즈니스 로직 - POST
	@RequestMapping(value = "AdminNoticeModifyPro.admin", method = RequestMethod.POST)
	public String modifyNotice(@ModelAttribute AdminNoticeDTO noticeList, @RequestParam int pageNum, Model model) {
		boolean isUpdateSuccess = service.modifyNotice(noticeList);
		System.out.println(noticeList);
		if (!isUpdateSuccess) {
			model.addAttribute("msg", "수정실패!");
			return "fail_back";
		}

		model.addAttribute("notice_idx", noticeList.getNotice_idx());
		model.addAttribute("pageNum", pageNum);

		return "redirect:/AdminNoticeView.admin";
	}

}
