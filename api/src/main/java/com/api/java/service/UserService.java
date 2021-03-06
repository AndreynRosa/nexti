package com.api.java.service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.java.dao.UserDao;
import com.api.java.model.dto.AuthenticatedUserDto;
import com.api.java.model.dto.UserDto;
import com.api.java.model.entity.Product;
import com.api.java.model.entity.User;

@Service
public class UserService {

	@Autowired
	private UserDao dao;

	public User save(User userRequest) {
		if (null == userRequest.getId()) {
			return dao.save(userRequest);
		} else if (updateUser(userRequest)) {
			return dao.save(userRequest);
		}
		return null;
	}

	private boolean updateUser(User userRequest) {
		return userRequest.getEmail() != null && userRequest.getPassword() != null;
	}

	public User remove(Integer id) {
		User user = findById(id);
		if (null != user) {
			dao.delete(user);
			user.setProducts(null);
			return (user);
		}
		return null;
	}

	public User findById(Integer id) {
		Optional<User> opt = dao.findById(id);
		return opt.orElse(null);
	}

	public AuthenticatedUserDto login(UserDto userRequest) {
		if (null != userRequest.getEmail() && null != userRequest.getPassword()) {
			return getAuthenticatedUser(userRequest);
		} else {
			return null;
		}
	}

	private AuthenticatedUserDto getAuthenticatedUser(UserDto userRequest) {
		User user = findByEmail(userRequest.getEmail());
		AuthenticatedUserDto autUser = new AuthenticatedUserDto();
		if (isAuthenticatedUser(userRequest, user)) {
			autUser.setEmail(user.getEmail());
			autUser.setId(user.getId());

			return autUser;
		} else {
			return null;
		}
	}

	private boolean isAuthenticatedUser(UserDto userRequest, User user) {
		return null != user && user.getPassword().equals(userRequest.getPassword());
	}

	private User findByEmail(String email) {

		return dao.findByIdAndEmail(email);
	}

	public boolean exisits(Integer userId) {

		return dao.existsById(userId);
	}

	public Product handlingPrductUser(Product product) {
		product.getUser().setProducts(null);
		return product;
	};

}
