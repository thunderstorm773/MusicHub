package com.tu.musichub.user.services;

import com.tu.musichub.user.entities.Role;
import com.tu.musichub.user.models.viewModels.RoleView;
import com.tu.musichub.user.repositories.RoleRepository;
import com.tu.musichub.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class RoleServiceImpl {

    private final RoleRepository roleRepository;

    private final MapperUtil mapperUtil;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository,
                           MapperUtil mapperUtil) {
        this.roleRepository = roleRepository;
        this.mapperUtil = mapperUtil;
    }

    public RoleView findByName(String roleName) {
        Role role = this.roleRepository.findByAuthority(roleName);
        if (role == null) {
            return null;
        }

        return this.mapperUtil.getModelMapper().map(role, RoleView.class);
    }

    public List<RoleView> findAll() {
        List<Role> roles = this.roleRepository.findAll();
        return this.mapperUtil.convertAll(roles, RoleView.class);
    }
}
