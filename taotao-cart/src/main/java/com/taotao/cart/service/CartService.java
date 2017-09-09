package com.taotao.cart.service;

import com.github.abel533.entity.Example;
import com.taotao.cart.bean.User;
import com.taotao.cart.mapper.CartMapper;
import com.taotao.cart.pojo.Cart;
import com.taotao.cart.pojo.Item;
import com.taotao.cart.util.LocalUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Ellen on 2017/7/11.
 */
@Service
public class CartService {

    @Autowired
    private ItemService itemService;

    @Autowired
    private CartMapper cartMapper;

    public void addItemToCart(long itemId) {
        //判断该商品在购物车中是否存在
        User user = LocalUser.getUser();
        Cart record = new Cart();
        record.setItemId(itemId);
        record.setUserId(user.getId());
        Cart cart = cartMapper.selectOne(record);
        Item item = itemService.queryItemBuId(itemId);
        if (cart == null) {
            cart = new Cart();
            cart.setUserId(user.getId());
            cart.setItemId(itemId);
            cart.setUpdated(new Date());
            cart.setCreated(new Date());
            cart.setUpdated(cart.getCreated());
            cart.setNum(1);
            cart.setItemImage(item.getImage());
            cart.setItemPrice(item.getPrice());
            cart.setItemTitle(item.getTitle());
            cartMapper.insert(cart);
        } else {
            //存在
            cart.setNum(cart.getNum() + 1);
            cart.setUpdated(new Date());
            cart.setItemPrice(cart.getItemPrice() + item.getPrice());
            cartMapper.updateByPrimaryKeySelective(cart);
        }
    }

    public List<Cart> queryList() {
        return queryList(LocalUser.getUser().getId());
    }

    public List<Cart> queryList(long userId) {
        Example example = new Example(Cart.class);
        example.setOrderByClause("created DESC");
        example.createCriteria().andEqualTo("userId", userId);
        return cartMapper.selectByExample(example);
    }

    public void updateNum(int num, long itemId) {
        //更新的数据
        Cart cart = new Cart();
        cart.setNum(num);
        cart.setItemId(itemId);
        //更新的条件
        Example example = new Example(Cart.class);
        example.createCriteria().andEqualTo("itemId", itemId).
                andEqualTo("userId", LocalUser.getUser().getId());

        cartMapper.updateByExampleSelective(cart, example);
    }

    public void deleteCart(long itemId) {
        Cart cart = new Cart();
        cart.setItemId(itemId);
        cartMapper.delete(cart);
    }

    public boolean deleteAll(long userId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        int delete = cartMapper.delete(cart);
        return delete > 0;
    }
}
