package cloud.rsqaured.component;

import cloud.rsqaured.exception.AccessDeniedException;
import cloud.rsqaured.persistence.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;


import java.util.Objects;

@Component
public class PermissionsValidator {

    private AuthenticatedUserResolver userResolver;
    @Autowired
    public PermissionsValidator(
            AuthenticatedUserResolver userResolver
    ) {
        this.userResolver = userResolver;

    }

//    public void validateAgent(UserEntity callingUserEntity){
//        AgentEntity agentEntity = agentRepository.findByUserEntity(callingUserEntity);
//        if(Objects.isNull(agentEntity)) throw new AccessDeniedException();
//    }
//    public void validateAgentOrSystemAdmin(UserEntity callingUserEntity){
////        AgentEntity agentEntity = agentRepository.findByUserEntity(callingUserEntity);
//        if(!callingUserEntity.isSystemAdministrator() || Objects.isNull(agentRepository.findByUserEntity(callingUserEntity))) throw new AccessDeniedException();
//    }
//    public void validateAgentOrSystemAdminOrClient(UserEntity callingUserEntity){
//        if(!callingUserEntity.isSystemAdministrator() ||
//                Objects.isNull(agentRepository.findByUserEntity(callingUserEntity)) ||
//                Objects.isNull(clientRepository.findByUserEntity(callingUserEntity))
//        )
//            throw new
//                    AccessDeniedException();
//    }
//    public void validateCorrectUserEntity(UserEntity userEntity1, UserEntity userEntity2){
//        Integer userId1 = userEntity1.getId();
//        Integer userId2 = userEntity2.getId();
//        if(!userId1.equals(userId2)) throw new AccessDeniedException();
//    }

}
