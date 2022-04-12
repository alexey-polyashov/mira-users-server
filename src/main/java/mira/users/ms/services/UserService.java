package mira.users.ms.services;

import lombok.NoArgsConstructor;
import mira.users.ms.dto.*;
import mira.users.ms.entity.RoleModel;
import mira.users.ms.entity.UserModel;
import mira.users.ms.exceptions.BadRequestException;
import mira.users.ms.exceptions.NotFoundException;
import mira.users.ms.repositories.RoleRepository;
import mira.users.ms.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
public class UserService implements UserDetailsService {

    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  ModelMapper modelMapper;
    @Autowired
    private  BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private  RoleRepository roleRepository;

    public List<UserModel> findAll() {
        return userRepository.findAll();
    }

    public UserModel findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(()->new NotFoundException(String.format("User with id = %d", userId)));
    }

    public Long newUser(NewUserDto userDto) {
        UserModel user = modelMapper.map(userDto, UserModel.class);
        user.setPassword(
                passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);
        return user.getId();
    }

    public Long saveUser(Long userId, UserUpdateDto userDto) {
        UserModel user = findById(userId);
        user.setEmail(userDto.getEmail());
        user.setLogin(userDto.getLogin());
        userRepository.save(user);
        return user.getId();
    }

    public void deleteUser(Long userId) {
        findById(userId); //check user id
        userRepository.deleteById(userId);
    }

    public Long setPassword(Long userId, SetPasswordDto passwordDto) {
        UserModel user = findById(userId);
        if(!passwordEncoder.matches(passwordDto.getOldPassword(), user.getPassword())){
            throw new BadRequestException("Old password incorrect");
        }
        user.setPassword(
                passwordEncoder.encode(passwordDto.getNewPassword()));
        userRepository.save(user);
        return user.getId();
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<RoleModel> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(
                role.getName())).collect(Collectors.toList());
    }

    public UserModel findByLogin(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with login '%s' not found", login)));
    }

    public UserDetails getUserDetail(String login) {
        UserModel user = findByLogin(login);
        return new User(user.getLogin(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    public UserModel findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(String.format("User with email '%s' not found", email)));

    }

    public Set<RoleDto> getUserRoles(Long userId) {
        UserModel user = findById(userId);
        return user.getRoles().stream()
                .map(p->modelMapper.map(p, RoleDto.class))
                .collect(Collectors.toSet());
    }

    @Transactional
    public Set<RoleDto> setUserRoles(Long userId, Set<String> roles) {
        UserModel user = findById(userId);
        user.getRoles().clear();
        Set<RoleModel> roleList = roleRepository.findByNameIn(roles.toArray(new String[0]));
        user.setRoles(roleList);
        userRepository.save(user);
        return user.getRoles().stream()
                .map(p->modelMapper.map(p, RoleDto.class))
                .collect(Collectors.toSet());
    }

    public Set<RoleDto> deleteUserRole(Long userId, String roleName) {
        UserModel user = findById(userId);
        user.setRoles(user.getRoles().stream()
                .filter(p->!roleName.equals(p.getName()))
                .collect(Collectors.toSet()));
        userRepository.save(user);
        return user.getRoles().stream()
                .map(p->modelMapper.map(p, RoleDto.class))
                .collect(Collectors.toSet());
    }

    public Set<RoleDto> addUserRoles(Long userId, Set<String> roles) {
        UserModel user = findById(userId);
        Set<RoleModel> roleList = roleRepository.findByNameIn(roles.toArray(new String[0]));
        Set<RoleModel> currentRoles = user.getRoles();
        currentRoles.addAll(roleList);
        userRepository.save(user);
        return user.getRoles().stream()
                .map(p->modelMapper.map(p, RoleDto.class))
                .collect(Collectors.toSet());
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserModel user = findByLogin(userName);
        return new User(user.getLogin(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }
}
