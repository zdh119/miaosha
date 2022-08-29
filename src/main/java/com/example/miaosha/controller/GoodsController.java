package com.example.miaosha.controller;

import com.example.miaosha.pojo.MiaoshaGoods;
import com.example.miaosha.pojo.MiaoshaUser;
import com.example.miaosha.redis.GoodsKey;
import com.example.miaosha.result.Result;
import com.example.miaosha.service.GoodsService;
import com.example.miaosha.service.MiaoshaUserService;
import com.example.miaosha.util.RedisUtil;
import com.example.miaosha.vo.GoodsDetailVo;
import com.example.miaosha.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.IContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.context.webflux.SpringWebFluxContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


@Controller
@RequestMapping("/goods")
public class GoodsController {

	@Autowired
    MiaoshaUserService miaoshaUserService;

	@Autowired
    GoodsService goodsService;

	@Autowired
    RedisUtil redisUtil;

	@Autowired
    ThymeleafViewResolver thymeleafViewResolver;

	@Autowired
    ApplicationContext applicationContext;

//    @RequestMapping("/to_list")
//    public String list(
//            HttpServletResponse response,
//            Model model,
//                       @CookieValue(value = "token", required = false) String cookieToken,
//                       @RequestParam(value = "token", required = false) String paramToken,
//                       MiaoshaUser user
//    ) {
//        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)){
//            return "login";
//        }
//        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
//        MiaoshaUser miaoshaUser = miaoshaUserService.getByToken(response, token);
//    	model.addAttribute("user", miaoshaUser);
//        return "goods_list";
//    }

    @RequestMapping(value = "/to_list", produces ="text/html")
    @ResponseBody
    public String list(HttpServletRequest request,
                       HttpServletResponse response,
                       Model model,
                       MiaoshaUser user) {
        model.addAttribute("user", user);
        // 取缓存
        String html = (String) redisUtil.get(GoodsKey.getGoodsList, "gl");
        if (!StringUtils.isEmpty(html)){
            return html;
        }
        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsVoList);
        /**
         * 手动渲染
         */
        IContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
        if (!StringUtils.isEmpty(html)){
            redisUtil.set(GoodsKey.getGoodsList, "gl", html);
        }
        return html;
    }

    @RequestMapping(value = "/to_detail2/{goodsId}", produces = "text/html")
    @ResponseBody
    public String detail2(
            HttpServletRequest request,
            HttpServletResponse response,
            Model model,
            MiaoshaUser miaoshaUser,
            @PathVariable("goodsId")long goodsId){
        model.addAttribute("user", miaoshaUser);

        // 取缓存
        String html =(String) redisUtil.get(GoodsKey.getGoodsDetail, ""+goodsId);
        if (!StringUtils.isEmpty(html)){
            return html;
        }
        // 手动渲染
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goodsVo);

        long startAt = goodsVo.getStartDate().getTime();
        long endAt = goodsVo.getEndDate().getTime();
        long now = System.currentTimeMillis();
        // 秒杀状态(0: 未开始; 1: 进行中; 2:已结束)
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if (now<startAt){
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt-now)/1000);
        } else if (now>endAt){
            miaoshaStatus = 2;
            remainSeconds = -1;
        } else {
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);

        /**
         * 修改
         */
        IContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", ctx);
        if (!StringUtils.isEmpty(html)){
            redisUtil.set(GoodsKey.getGoodsDetail, ""+goodsId, html);
        }
        return html;
    }

    @RequestMapping("/to_detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> detail(
            HttpServletRequest request,
            HttpServletResponse response,
            Model model,
            MiaoshaUser user,
            @PathVariable("goodesId")long goodsId){

        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);


        long startAt = goodsVo.getStartDate().getTime();
        long endAt = goodsVo.getEndDate().getTime();
        long now = System.currentTimeMillis();
        // 秒杀状态(0: 未开始; 1: 进行中; 2:已结束)
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if (now<startAt){
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt-now)/1000);
        } else if (now>endAt){
            miaoshaStatus = 2;
            remainSeconds = -1;
        } else {
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        GoodsDetailVo vo = new GoodsDetailVo();
        vo.setGoods(goodsVo);;
        vo.setUser(user);
        vo.setRemainSeconds(remainSeconds);
        vo.setMiaoshaStatus(miaoshaStatus);

        return Result.success(vo);
    }

}
