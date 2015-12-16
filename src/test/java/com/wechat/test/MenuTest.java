package com.wechat.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.wechat.entity.Partner;
import com.wechat.manager.PartnerMng;
import com.wechat.plugins.WechatConfigSvc;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;

public class MenuTest {

	public static void main(String[] args) throws WxErrorException {
		ApplicationContext applicationContext = new FileSystemXmlApplicationContext(
				"classpath:/com/wechat/spring/application/application-context.xml");

		WechatConfigSvc wechatConfigSvc = applicationContext
				.getBean(WechatConfigSvc.class);
		
		PartnerMng partnerMng = applicationContext.getBean(PartnerMng.class);
		Partner partner = partnerMng.get(1L);
		WxMpService wxMpService = wechatConfigSvc.createWxMpService(
				partner.getAppId(), partner.getSecretKey(), partner.getToken());
		wxMpService.menuDelete();
		
		
		
		
//		WxMenu wxMenu = new WxMenu();
//		List<WxMenuButton> menuButtons = new ArrayList<WxMenu.WxMenuButton>();
//		WxMenuButton menuButton1 = new WxMenuButton();
//		menuButton1.setName("微商城");
//		menuButton1.setType("view");
//		menuButton1.setUrl("http://www.rosepie.com");
//		menuButtons.add(menuButton1);
//		WxMenuButton menuButton2 = new WxMenuButton();
//		menuButton2.setName("资讯");
//		WxMenuButton menuButton2_1 = new WxMenuButton();
//		menuButton2_1.setType("view");
//		menuButton2_1.setName("资讯");
//		menuButton2_1.setUrl("http://www.rosepie.com/cms/");
//		menuButton2.getSubButtons().add(0, menuButton2_1);
//		WxMenuButton menuButton2_5 = new WxMenuButton();
//		menuButton2_5.setType("view");
//		menuButton2_5.setName("私蜜空间");
//		menuButton2_5
//				.setUrl("http://www.rosepie.com/cms/001a1/569.htm");
//		menuButton2.getSubButtons().add(1, menuButton2_5);
//		WxMenuButton menuButton2_4 = new WxMenuButton();
//		menuButton2_4.setType("view");
//		menuButton2_4.setName("Bulgarian Rose");
//		menuButton2_4
//				.setUrl("http://www.rosepie.com/cms/001a1/570.htm");
//		menuButton2.getSubButtons().add(2, menuButton2_4);
//		WxMenuButton menuButton2_2 = new WxMenuButton();
//		menuButton2_2.setType("view");
//		menuButton2_2.setName("经销商查询");
//		menuButton2_2.setUrl("http://www.rosepie.com/query/query.html");
//		menuButton2.getSubButtons().add(3, menuButton2_2);
//		WxMenuButton menuButton2_3 = new WxMenuButton();
//		menuButton2_3.setType("view");
//		menuButton2_3.setName("防伪码查询");
//		menuButton2_3.setUrl("http://www.rosepie.com/query/qrcode.html");
//		menuButton2.getSubButtons().add(4, menuButton2_3);
//		menuButtons.add(menuButton2);
//		WxMenuButton menuButton3 = new WxMenuButton();
//		menuButton3.setName("个人中心");
//		menuButton3.setType("view");
//		menuButton3.setUrl("http://www.rosepie.com/member/index.html");
//		menuButtons.add(menuButton3);
//		wxMenu.setButtons(menuButtons);
//		wxMpService.menuCreate(wxMenu);
	}
}
