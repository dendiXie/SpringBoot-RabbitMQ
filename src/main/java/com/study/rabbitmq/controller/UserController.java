package com.study.rabbitmq.controller;

import com.study.rabbitmq.mq.RabbitConfig;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.study.rabbitmq.model.User;
import com.study.rabbitmq.service.UserService;

import java.util.UUID;

@Controller
@RequestMapping(value = "user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private AmqpTemplate rabbitTemplate;

	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public User getUser(@PathVariable("id") String id) {

		// String message = "1"; // 模拟获取用户id为1
		// rabbitTemplate.convertAndSend(RabbitConfig.DIRECT_ROUTING_KEY_recvqueue, message);

		// 1查询db
		User user = userService.getUser(id);

		// 2发送短信

		// 利用mq，实现异步
		// mq.sendMesaage(1);

		//user.setUserAddress(beanInject.getDatapath());
		return user;
		// 压测： jmeter , loadruner, ab, 自己写代码实现（模拟多线程并发）
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	@ResponseBody
	public int addUser(User user) {
		user.setUserName(UUID.randomUUID().toString());
		user.setUserAge(Math.random()+"");
		user.setUserAddress(UUID.randomUUID().toString());
		return userService.addUser(user);
	}

}
