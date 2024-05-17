package com.enigma.dolen.service;

import com.enigma.dolen.constant.ERole;
import com.enigma.dolen.model.entity.Role;

public interface RoleService {

    Role getOrSave(ERole role);
}
