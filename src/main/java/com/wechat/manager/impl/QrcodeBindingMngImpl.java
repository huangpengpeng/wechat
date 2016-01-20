package com.wechat.manager.impl;

import java.util.Map;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.common.util.StringUtils;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.wechat.dao.QrcodeBindingDao;
import com.wechat.entity.Partner;
import com.wechat.entity.Qrcode;
import com.wechat.entity.QrcodeBinding;
import com.wechat.entity.WechatUser;
import com.wechat.manager.PartnerMng;
import com.wechat.manager.QrcodeBindingMng;
import com.wechat.manager.QrcodeMng;
import com.wechat.manager.WechatUserMng;
import com.wechat.plugins.WechatConfigSvc;

@Transactional(isolation = Isolation.REPEATABLE_READ)
@Service
public class QrcodeBindingMngImpl implements QrcodeBindingMng, InitializingBean {
	
	private Logger log=LoggerFactory.getLogger(QrcodeBindingMngImpl.class);

	public void add(Long partnerId, Long qrcodeId, Long wechatUserId) {
		QrcodeBinding qrcodeBinding = new QrcodeBinding(partnerId, qrcodeId,
				wechatUserId);
		qrcodeBinding.init();
		dao.add(qrcodeBinding);
	}

	public QrcodeBinding getByQrcode(Long partnerId, Long wechatUserId) {
		return dao.getByQrcode(partnerId, wechatUserId);
	}
	
	@Override
	public void delete(Long id) {
		dao.delete(id);
	}

	public void afterPropertiesSet() throws Exception {
		wechatConfigSvc.addMsgHandler(WxConsts.XML_MSG_EVENT, new String[] {
				WxConsts.EVT_SUBSCRIBE, WxConsts.EVT_SCAN }, null, null, null,
				new WxMpMessageHandler() {

					public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
							Map<String, Object> context,
							WxMpService wxMpService,
							WxSessionManager sessionManager)
							throws WxErrorException {
						String token = wxMessage.getToUserName();
						Partner partner = partnerMng.getByToken(token);
						String eventKey = wxMessage.getEventKey().replace(
								"qrscene_", "");
						log.info("wechat event tolen:{} eventKey:{}", token,
								eventKey);
						if (StringUtils.isBlank(eventKey)) {
							return null;
						}
						if (!NumberUtils.isNumber(eventKey)) {
							return null;
						}
						Qrcode qrcode = qrcodeMng.get(Long.valueOf(eventKey));
						if (qrcode == null) {
							return null;
						}
						String fromUserName = wxMessage.getFromUserName();
						WechatUser wechatUser = wechatUserMng.getByOpenId(
								partner.getId(), fromUserName);
						if (wechatUser == null) {
							wechatUser = (WechatUser) wechatUserMng.register(
									partner.getId(), null, fromUserName,
									Long.valueOf(qrcode.getSign())).get(
									"WECHAT_USER");
						}
						QrcodeBinding qrcodeBinding = dao.getByQrcode(
								partner.getId(), wechatUser.getId());
						if (qrcodeBinding == null) {
							add(partner.getId(), qrcode.getId(),
									wechatUser.getId());
						}
						return null;
					}
				});
	}

	@Autowired
	private QrcodeMng qrcodeMng;
	@Autowired
	private WechatUserMng wechatUserMng;
	@Autowired
	private PartnerMng partnerMng;
	@Autowired
	private WechatConfigSvc wechatConfigSvc;
	@Autowired
	private QrcodeBindingDao dao;
}
