<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>item/item_detail.jsp</title>
<script src="resources/js/jquery-3.6.0.js"></script>
<script type="text/javascript">
	$(function() {
		$("#purchase_item_quantity").html("<select name='order_quantity'></select>");
		for(let i = 1; i <= ${itemDetail.get('item_quantity')}; i++) {
			$("#purchase_item_quantity > select").append("<option value=" + i + ">" + i + "</option>");
		}
	});

</script>
<style type="text/css">
	img {
		width: 200px;
	}
</style>

</head>
<body>
	<div>
		<!-- 상단 부분 -->
		<jsp:include page="../header_footer/top.jsp"/>
	</div>
	
	<!-------- 본문 -------->
	<div>
		<hr>
		<table border="1">
			<tr>
				<th>상품 이미지</th>
				<td>
					<c:forEach items="${imgList }" var="item">
						<c:choose>
							<c:when test="${item.getImg_name() != null}">
								<img src="resources/upload/file/${item.getImg_name() }"><br>
							</c:when>
						</c:choose>
					</c:forEach>
				</td>
			</tr>
			<tr>
				<th>상품명</th>
				<td>${itemDetail.get('item_title')}</td>
			</tr>
			<tr>
				<th>상품 소개</th>
				<td>${itemDetail.get('item_content')}</td>
			</tr>
			<tr>
				<th>가격</th>
				<td>${itemDetail.get('item_price')}</td>
			</tr>
			<tr>
				<th>수량</th>
				<td>${itemDetail.get('item_quantity')}</td>
			</tr>
			<tr>
				<th>상품 등록일</th>
				<td>${itemDetail.get('item_date')}</td>
			</tr>
			<tr>
				<th>판매 상태</th>
				<td>${itemDetail.get('item_status')}</td>
			</tr>
			<tr>
				<th>찜</th>
				<td>${itemDetail.get('COUNT(w.member_id)')}</td>
			</tr>
			<tr>
				<th>조회수</th>
				<td>${itemDetail.get('item_readCnt')}</td>
			</tr>
			<tr>
				<th>카테고리</th>
				<td>${itemDetail.get('category_big')} > ${itemDetail.get('category_small')}</td>
			</tr>
			<tr>
				<th>거래지역</th>
				<td>${itemDetail.get('item_region')}</td>
			</tr>
		</table>
		
		<hr>
		
		<form action="PurchaseItem" method="post">
			<input type="hidden" name="item_idx" value="${itemDetail.get('item_idx')}">	
			<input type="hidden" name="img_name" value="${item.getImg_name() }">
			<table>
				<tr>
					<th>구매수량</th>
					<td id="purchase_item_quantity"></td>
				</tr>
				<tr>
					<td colspan="2"><input type="submit" value="구매하기"></td>
				</tr>
			</table>
		</form>
	</div>
	<!---------------------->
	
	<div>
		<!-- 하단 부분 -->
		<jsp:include page="../header_footer/bottom.jsp"/>
	</div>
</body>
</html>