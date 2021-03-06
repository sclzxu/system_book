package com.book.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.book.dao.BookInfoMapper;
import com.book.dao.CartMapper;
import com.book.dao.CategoryMapper;
import com.book.pojo.BookInfo;
import com.book.pojo.Cart;
import com.book.pojo.Category;
import com.book.tools.MyBatisUtil;

public class BookService {
	// 每页数量
	public final Integer PAGESIZE = 3;
	/**
	 * 添加新的分类
	 * @param category
	 * @return 1-成功 0-失败
	 */
	public int addNewCategory(String category) {
		// 保存返回结果
		int result = 0;
		// 去除两端空格
		String name = category.trim();
		SqlSession sqlSession = MyBatisUtil.open();
		// 查询此分类是否存在
		Category res = sqlSession.getMapper(CategoryMapper.class).findCategoryByName(name);
		// 不存在此分类
		if(res == null) {
			// 添加分类到数据库
			result = sqlSession.getMapper(CategoryMapper.class).addNewCategory(name);
		}
		// 提交事务（增删改）
		sqlSession.commit();
		MyBatisUtil.close(sqlSession);
		// 返回结果
		return result;
	}
	/**
	 * 获取所有的分类信息
	 * @return
	 */
	public List<Category> listCategories(){
		SqlSession sqlSession = MyBatisUtil.open();
		List<Category> categories = sqlSession
				.getMapper(CategoryMapper.class).listCategories();
		sqlSession.commit();
		MyBatisUtil.close(sqlSession);
		return categories;
	}
	/**
	 * 根据id删除分类
	 * @param id
	 * @return 1-成功 0-失败
	 */
	public int deleteCategoryById(Integer id) {
		int result = 0;
		SqlSession sqlSession = MyBatisUtil.open();
		result = sqlSession.getMapper(CategoryMapper.class).deleteCategoryById(id);
		sqlSession.commit();
		MyBatisUtil.close(sqlSession);
		return result;
	}
	/**
	 * 添加新书籍到数据库中
	 * @param book
	 * @return 1-成功 0-失败
	 */
	public int addNewBook(BookInfo book) {
		int result = 0;
		SqlSession sqlSession = MyBatisUtil.open();
		result = sqlSession.getMapper(BookInfoMapper.class).addNewBook(book);
		sqlSession.commit();
		sqlSession.close();
		return result;
	}
	/**
	 * 查询书籍信息
	 * @param currentPage--当前页
	 * @param category--分类名称
	 * @param bookName--搜索的图书名（模糊查询）
	 * @return 书籍信息列表
	 */
	public List<BookInfo> listBook(Integer currentPage,String category,
			String bookName){
		SqlSession sqlSession = MyBatisUtil.open();
		List<BookInfo> result = sqlSession.getMapper(BookInfoMapper.class)
				.listBook((currentPage-1)*PAGESIZE,PAGESIZE,category,bookName);
		sqlSession.close();
		return result;
	}
	/**
	 * 返回书籍数量
	 * @param category--分类名称
	 * @param bookName--搜索的图书名（模糊查询）
	 * @return
	 */
	public Integer bookCount(String category,String bookName) {
		SqlSession sqlSession = MyBatisUtil.open();
		int result = sqlSession.getMapper(BookInfoMapper.class)
				.bookCount(category,bookName);
		sqlSession.close();
		return result;
	}
	/**
	 * 返回书籍分页导航字符串
	 * @param currentPage--当前页码
	 * @param count--总共书籍数量
	 * @param where--跳转的servlet名称
	 * @return
	 */
	public String bookNavStr(Integer currentPage,Integer count,String where) {
		// 求得总共页数
		Integer countPage = count%PAGESIZE==0?count/PAGESIZE:count/PAGESIZE+1;
		if(currentPage==1 && countPage!=1) {
			return "<span class='fr'><a href='"+where+"?currentPage=1'>首页</a>&nbsp;<a>上一页</a>&nbsp;<a href='"+where+"?currentPage=2'>下一页</a>&nbsp;<a href='"+where+"?currentPage="+countPage+"'>尾页</a>&nbsp;</span>"; 
		}
		else if(currentPage==countPage && countPage!=1) {
			return "<span class='fr'><a href='"+where+"?currentPage=1'>首页</a>&nbsp;<a href='"+where+"?currentPage="+(currentPage-1)+"'>上一页</a>&nbsp;<a>下一页</a>&nbsp;<a href='"+where+"?currentPage="+countPage+"'>尾页</a>&nbsp;</span>";
		}
		else if(countPage == 1) {
			return "<span class='fr'><a href='"+where+"?currentPage=1'>首页</a>&nbsp;<a>上一页</a>&nbsp;<a>下一页</a>&nbsp;<a href='"+where+"?currentPage="+countPage+"'>尾页</a>&nbsp;</span>";
		}
		else {
			return "<span class='fr'><a href='"+where+"?currentPage=1'>首页</a>&nbsp;<a href='"+where+"?currentPage="+(currentPage-1)+"'>上一页</a>&nbsp;<a href='"+where+"?currentPage="+(currentPage+1)+"'>下一页</a>&nbsp;<a href='"+where+"?currentPage="+countPage+"'>尾页</a>&nbsp;</span>";
		}
	}
	/**
	 * 根据id删除图书
	 * @param id--图书Id
	 * @return 0-失败 1-成功
	 */
	public int deleteBookById(Integer id) {
		int result = 0;
		SqlSession sqlSession = MyBatisUtil.open();
		result = sqlSession.getMapper(BookInfoMapper.class).deleteBookById(id);
		sqlSession.commit();
		sqlSession.close();
		return result;
	}
	/**
	 * 根据图书id查找对应图书
	 * @param id
	 * @return null-失败  成功返回图书对象
	 */
	public BookInfo findBookById(Integer id) {
		BookInfo book = null;
		SqlSession sqlSession = MyBatisUtil.open();
		book = sqlSession.getMapper(BookInfoMapper.class).findBookById(id);
		sqlSession.commit();
		sqlSession.close();
		return book;
	}
	/**
	 * 根据id修改图书信息
	 * @param book--图书对象
	 * @return 1-成功 0-失败
	 */
	public int alterBookById(BookInfo book) {
		int result = 0;
		SqlSession sqlSession = MyBatisUtil.open();
		result = sqlSession.getMapper(BookInfoMapper.class).alterBookById(book);
		sqlSession.commit();
		sqlSession.close();
		return result;
	}
	/**
	 * 根据书本id和用户Id查找购物车中是否存在商品
	 * @param bid--书本id
	 * @param uid--用户id
	 * @return 成功返回对象，失败返回null
	 */
	public Cart findCartByBidAndUid(Integer bid,String uid) {
		Cart cart = null;
		SqlSession sqlSession = MyBatisUtil.open();
		cart = sqlSession.getMapper(CartMapper.class).findCartByBidAndUid(bid, uid);
		sqlSession.commit();
		sqlSession.close();
		return cart;
	}
	/**
	 * 添加新的购物车记录
	 * @param cart--新的商品
	 * @return 1-成功 0-失败
	 */
	public int addCart(Cart cart) {
		int result = 0;
		SqlSession sqlSession = MyBatisUtil.open();
		result = sqlSession.getMapper(CartMapper.class).addCart(cart);
		sqlSession.commit();
		sqlSession.close();
		return result;
	}
	/**
	 * 根据书籍Id和用户id修改对应购物车中商品数量
	 * @param bid--书籍id
	 * @param uid--用户id
	 * @return 成功1，失败0
	 */
	public int alterCart(Integer bid,String uid) {
		int result = 0;
		SqlSession sqlSession = MyBatisUtil.open();
		result = sqlSession.getMapper(CartMapper.class).alterCart(bid, uid);
		sqlSession.commit();
		sqlSession.close();
		return result;
	}
	/**
	 * 根据用户id获取其购物车中的所有商品
	 * @param uid--用户id
	 * @return 用户的购物车中的所有商品
	 */
	public List<Cart> findCartsByUid(String uid){
		List<Cart> carts = null;
		SqlSession sqlSession = MyBatisUtil.open();
		carts = sqlSession.getMapper(CartMapper.class).findCartsByUid(uid);
		sqlSession.commit();
		sqlSession.close();
		return carts;
	}
	/**
	 * 根据购物车id移除商品
	 * @param id--购物车中的编码
	 * @return 1-成功 0-失败
	 */
	public int deleteCartById(Integer id) {
		int result = 0;
		SqlSession sqlSession = MyBatisUtil.open();
		result = sqlSession.getMapper(CartMapper.class).deleteCartById(id);
		sqlSession.commit();
		sqlSession.close();
		return result;
	}
}






















