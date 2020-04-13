package com.book.dao;

import org.apache.ibatis.annotations.Param;

import com.book.pojo.Cart;

public interface CartMapper {
	/**
	 * 根据书本id和用户Id查找购物车中是否存在商品
	 * @param bid--书本id
	 * @param uid--用户id
	 * @return 成功返回对象，失败返回null
	 */
	Cart findCartByBidAndUid(@Param("bid") Integer bid,@Param("uid") String uid);
	/**
	 * 添加新的购物车记录
	 * @param cart--新的商品
	 * @return 1-成功 0-失败
	 */
	int addCart(Cart cart);
	/**
	 * 根据书籍Id和用户id修改对应购物车中商品数量
	 * @param bid--书籍id
	 * @param uid--用户id
	 * @return 成功1，失败0
	 */
	int alterCart(@Param("bid") Integer bid,@Param("uid") String uid);
}
