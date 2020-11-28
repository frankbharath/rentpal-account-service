package com.rentpal.accounts.common;

import com.rentpal.accounts.dto.UserDTO;
import com.rentpal.accounts.model.User;

/**
 * The Class DTOModelMapper.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Sep 29, 2020 5:44:38 PM
 * This class converts domain model to DTO and vice versa.
 */

public class DTOModelMapper {
	
	/**
	 * Converts user domain model to user DTO.
	 *
	 * @param user the user
	 * @return the user DTO
	 */
	public static  UserDTO userModelDTOMapper(User user) {
		UserDTO userDTO=new UserDTO();
		userDTO.setEmail(user.getEmail());
		userDTO.setUserid(user.getId());
		userDTO.setCreationTime(Utils.getDate(user.getCreationTime()));
		userDTO.setVerified(user.isVerified());
		return userDTO;
	}
}