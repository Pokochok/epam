package by.epam.touragency.logic;

import by.epam.touragency.entity.Role;
import by.epam.touragency.entity.User;
import by.epam.touragency.entity.UserPrincipal;
import by.epam.touragency.exception.LogicException;
import by.epam.touragency.exception.RepositoryException;
import by.epam.touragency.repository.impl.UserRepository;
import by.epam.touragency.specification.Specification;
import by.epam.touragency.specification.impl.admin.*;
import by.epam.touragency.specification.impl.agent.*;
import by.epam.touragency.specification.impl.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static by.epam.touragency.util.PageMsgConstant.LOGGER;

/**
 * For user updates logic
 */
@Service
public class UpdateUserLogic {
    @Autowired
    private MatchOfUniqueFieldsDetector matchOfUniqueFieldsDetector;

    /**
     * Updates user email
     *
     * @param role  user role
     * @param email user email
     * @param login user login
     * @return true, if updating completed successfully, and false - if not
     * @throws LogicException if handled RepositoryException
     */
    public boolean updateEmail(String role, String email, String login) throws LogicException {
        boolean flag = false;
        Specification specification = defineSpecificationForEmail(role, email, login);

        UserRepository repository = UserRepository.getInstance();
        try {
            if (flag = !(matchOfUniqueFieldsDetector.isExistsEmail(email)
                    || !matchOfUniqueFieldsDetector.isExistsLogin(login))) {
                repository.update(null, specification);
            }
        } catch (RepositoryException e) {
            throw new LogicException(e);
        }
        return flag;
    }

    /**
     * Defines specification for email updating
     *
     * @param role  role
     * @param email email
     * @param login login
     * @return specification
     * @throws LogicException if if user role is not defined
     */
    private Specification defineSpecificationForEmail(String role, String email, String login) throws LogicException {
        Specification specification = null;
        switch (Role.valueOf(role)) {
            case AGENT: {
                specification = new UpdateAgentEmailByLoginSpecification(email, login);
                break;
            }
            case CLIENT: {
                specification = new UpdateClientEmailByLoginSpecification(email, login);
                break;
            }
            case ADMIN: {
                specification = new UpdateAdminEmailByLoginSpecification(email, login);
                break;
            }
            default: {
                LOGGER.error("User role is not defined while updating email");
                throw new LogicException("User role is not defined");
            }
        }
        return specification;
    }

    /**
     * Updates phone number
     *
     * @param role           role
     * @param newPhoneNumber new phone number
     * @param login          login
     * @return true, if updating completed successfully, and false - if not
     * @throws LogicException if handled RepositoryException
     */
    public boolean updatePhoneNumber(String role, String newPhoneNumber, String login) throws LogicException {
        boolean flag = false;
        Specification specification = defineSpecificationForPhoneNumber(role, newPhoneNumber, login);

        UserRepository repository = UserRepository.getInstance();
        try {
            if (flag = !(matchOfUniqueFieldsDetector.isExistsPhoneNumber(newPhoneNumber)
                    || !matchOfUniqueFieldsDetector.isExistsLogin(login))) {
                repository.update(null, specification);
            }
        } catch (RepositoryException e) {
            throw new LogicException(e);
        }
        return flag;
    }

    /**
     * Defines specification for phone number updating
     *
     * @param role           role
     * @param newPhoneNumber new phone number
     * @param login          login
     * @return specification
     * @throws LogicException if if user role is not defined
     */
    private Specification defineSpecificationForPhoneNumber(String role, String newPhoneNumber, String login) throws LogicException {
        Specification specification = null;
        switch (Role.valueOf(role)) {
            case AGENT: {
                specification = new UpdateAgentPhoneNumberByLoginSpecification(newPhoneNumber, login);
                break;
            }
            case CLIENT: {
                specification = new UpdateClientPhoneByLoginSpecification(newPhoneNumber, login);
                break;
            }
            case ADMIN: {
                specification = new UpdateAdminPhoneNumberByLoginSpecification(newPhoneNumber, login);
                break;
            }
            default: {
                LOGGER.error("User role is not defined while updating phone number");
                throw new LogicException("User role is not defined");
            }
        }
        return specification;
    }

    /**
     * Updates user login
     *
     * @param role  role
     * @param login login
     * @param email email
     * @return true, if updating completed successfully, and false - if not
     * @throws LogicException if handled RepositoryException
     */
    public boolean updateLogin(String role, String login, String email) throws LogicException {
        boolean flag = false;
        Specification specification = defineSpecificationForLogin(role, login, email);

        UserRepository repository = UserRepository.getInstance();
        try {
            if (flag = !(matchOfUniqueFieldsDetector.isExistsLogin(login)
                    || !matchOfUniqueFieldsDetector.isExistsEmail(email))) {
                repository.update(null, specification);
            }
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * Defines specification for login updating
     *
     * @param role  role
     * @param login login
     * @param email email
     * @return specification
     * @throws LogicException if if user role is not defined
     */
    private Specification defineSpecificationForLogin(String role, String login, String email) throws LogicException {
        Specification specification = null;
        switch (Role.valueOf(role)) {
            case AGENT: {
                specification = new UpdateAgentLoginByEmailSpecification(login, email);
                break;
            }
            case CLIENT: {
                specification = new UpdateClientLoginByEmailSpecification(login, email);
                break;
            }
            case ADMIN: {
                specification = new UpdateAdminLoginByEmailSpecification(login, email);
                break;
            }
            default: {
                LOGGER.error("User role is not defined while updating login");
                throw new LogicException("User role is not defined");
            }
        }
        return specification;
    }

    /**
     * Updates user password
     *
     * @param role        role
     * @param login       login
     * @param password    password
     * @param newPassword new password
     * @return true, if updating completed successfully, and false - if not
     * @throws LogicException if handled RepositoryException or if user role is not defined
     */
    public boolean updatePassword(String role, String login, String password, String newPassword) throws LogicException {
        String encryptedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(10));
        boolean flag = false;
        Specification specificationForValidate = null;
        Specification specification = null;
        User user= null;
        try {
            switch (Role.valueOf(role)) {
                case AGENT: {
                    specificationForValidate = new FindAgentByLoginSpecification(login);
                    specification = new UpdateAgentPasswordByLoginSpecification(login, encryptedNewPassword);
                    break;
                }
                case CLIENT: {
                    specificationForValidate = new FindClientByLoginSpecification(login);
                    specification = new UpdateClientPasswordByLoginSpecification(login, encryptedNewPassword);
                    break;
                }
                case ADMIN: {
                    specificationForValidate = new FindAdminByLoginSpecification(login);
                    specification = new UpdateAdminPasswordByLoginSpecification(login, encryptedNewPassword);
                    break;
                }
                default: {
                    LOGGER.error("User role is not defined while updating password");
                    throw new LogicException("User role is not defined");
                }
            }

            UserRepository repository = UserRepository.getInstance();
            user = repository.query(specificationForValidate).iterator().next();
            if (flag = BCrypt.checkpw(password, user.getPassword())) {
                repository.update(null, specification);
            }
        } catch (RepositoryException e) {
            throw new LogicException(e);
        }
        return flag;
    }

    /**
     * Updates user name
     *
     * @param role    role
     * @param login   login
     * @param newName new name
     * @return true, if updating completed successfully, and false - if not
     * @throws LogicException if handled RepositoryException
     */
    public boolean updateName(String role, String login, String newName) throws LogicException {
        Specification specification = defineSpecificationForName(role, newName, login);

        boolean flag = false;
        UserRepository repository = UserRepository.getInstance();
        try {
            if (flag = matchOfUniqueFieldsDetector.isExistsLogin(login)) {
                repository.update(null, specification);
            }
        } catch (RepositoryException e) {
            throw new LogicException(e);
        }
        return flag;
    }

    /**
     * Defines specification for usr name updating
     *
     * @param role    role
     * @param newName new name
     * @param login   login
     * @return specification
     * @throws LogicException if if user role is not defined
     */
    private Specification defineSpecificationForName(String role, String newName, String login) throws LogicException {
        Specification specification = null;
        switch (Role.valueOf(role)) {
            case AGENT: {
                specification = new UpdateAgentNameByLoginSpecification(newName, login);
                break;
            }
            case CLIENT: {
                specification = new UpdateClientNameByLoginSpecification(newName, login);
                break;
            }
            case ADMIN: {
                specification = new UpdateAdminNameByLoginSpecification(newName, login);
                break;
            }
            default: {
                LOGGER.error("User role is not defined while updating name");
                throw new LogicException("User role is not defined");
            }
        }
        return specification;
    }

    /**
     * Updates user surname
     *
     * @param role       role
     * @param login      login
     * @param newSurname new surname
     * @return true, if updating completed successfully, and false - if not
     * @throws LogicException if handled RepositoryException
     */
    public boolean updateSurname(String role, String login, String newSurname) throws LogicException {
        Specification specification = defineSpecificationForSurname(role, newSurname, login);

        boolean flag = false;
        UserRepository repository = UserRepository.getInstance();
        try {
            if (flag = matchOfUniqueFieldsDetector.isExistsLogin(login)) {
                repository.update(null, specification);
            }
        } catch (RepositoryException e) {
            throw new LogicException(e);
        }
        return flag;
    }

    /**
     * Defines specification for user surname Updating
     *
     * @param role       role
     * @param newSurname new surname
     * @param login      login
     * @return specification
     * @throws LogicException if user role is not defined
     */
    private Specification defineSpecificationForSurname(String role, String newSurname, String login) throws LogicException {
        Specification specification = null;
        switch (Role.valueOf(role)) {
            case AGENT: {
                specification = new UpdateAgentSurnameByLoginSpecification(newSurname, login);
                break;
            }
            case CLIENT: {
                specification = new UpdateClientSurnameByLoginSpecification(newSurname, login);
                break;
            }
            case ADMIN: {
                specification = new UpdateAdminSurnameByLoginSpecification(newSurname, login);
                break;
            }
            default: {
                LOGGER.error("User role is not defined while updating surname");
                throw new LogicException("User role is not defined");
            }
        }
        return specification;
    }

    public boolean checkPrincipal(){
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserPrincipal;
    }
}