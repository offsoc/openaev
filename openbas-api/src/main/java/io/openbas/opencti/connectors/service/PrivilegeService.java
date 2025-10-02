package io.openbas.opencti.connectors.service;

import static io.openbas.opencti.connectors.Constants.PROCESS_STIX_GROUP_ID;
import static io.openbas.opencti.connectors.Constants.PROCESS_STIX_ROLE_ID;

import io.openbas.database.model.Capability;
import io.openbas.database.model.Group;
import io.openbas.database.model.Role;
import io.openbas.database.model.User;
import io.openbas.opencti.connectors.ConnectorBase;
import io.openbas.rest.group.form.GroupCreateInput;
import io.openbas.rest.user.form.user.CreateUserInput;
import io.openbas.service.GroupService;
import io.openbas.service.RoleService;
import io.openbas.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PrivilegeService {
  private final RoleService roleService;
  private final GroupService groupService;
  private final UserService userService;

  @Transactional
  public void ensureRequiredPrivilegesExist() {
    createWellKnownGroupWithRole(createWellKnownRole());
  }

  @Transactional
  public void ensurePrivilegedUserExistsForConnector(ConnectorBase connector) {
    Group group = createWellKnownGroupWithRole(createWellKnownRole());

    Optional<User> connectorUser = userService.findByToken(connector.getAuthToken());
    if (connectorUser.isEmpty()) {
      CreateUserInput input = new CreateUserInput();
      input.setAdmin(false);
      input.setFirstname(connector.getName());
      input.setLastname("OpenCTI Connector");
      input.setToken(connector.getAuthToken());
      input.setEmail("connector-%s@openbas.invalid".formatted(connector.getId()));
      User u = userService.createUser(input, 1); // magic number; Active
      u.setGroups(new ArrayList<>(List.of(group)));
      userService.updateUser(u);
    }
  }

  private Role createWellKnownRole() {
    Optional<Role> processStixRole = roleService.findById(PROCESS_STIX_ROLE_ID);
    if (processStixRole.isEmpty()) {
      processStixRole =
          Optional.of(
              roleService.createRole(
                  PROCESS_STIX_ROLE_ID,
                  "STIX bundle processors",
                  Set.of(Capability.MANAGE_STIX_BUNDLE)));
    }
    return processStixRole.get();
  }

  private Group createWellKnownGroupWithRole(Role role) {
    Optional<Group> processStixGroup = groupService.findById(PROCESS_STIX_GROUP_ID);
    if (processStixGroup.isEmpty()) {
      GroupCreateInput input = new GroupCreateInput();
      input.setName("STIX bundle processors");
      input.setDescription("Group for granting access rights to the STIX bundle API");
      input.setDefaultUserAssignation(false);
      processStixGroup =
          Optional.of(
              groupService.createGroupWithRole(PROCESS_STIX_GROUP_ID, input, List.of(role)));
    }
    return processStixGroup.get();
  }
}
