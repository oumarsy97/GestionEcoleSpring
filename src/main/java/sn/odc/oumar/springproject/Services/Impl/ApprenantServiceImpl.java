package sn.odc.oumar.springproject.Services.Impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import sn.odc.oumar.springproject.Datas.Entity.Apprenant;
import sn.odc.oumar.springproject.Datas.Entity.PromoReferentiel;
import sn.odc.oumar.springproject.Datas.Entity.Role;
import sn.odc.oumar.springproject.Datas.Entity.User;
import sn.odc.oumar.springproject.Datas.Repository.Interfaces.*;
import sn.odc.oumar.springproject.Exceptions.ServiceException;
import sn.odc.oumar.springproject.Services.Interfaces.ApprenantService;
import sn.odc.oumar.springproject.Web.Dtos.Request.ApprenantDTO;
import sn.odc.oumar.springproject.Web.Mappers.ApprenantMapper;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;


@org.springframework.stereotype.Service
public class ApprenantServiceImpl extends BaseServiceImpl<Apprenant,Long> implements ApprenantService {
    protected ApprenantRepository apprenantRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final PromoReferentielRepository promotionReferentielRepositoryImpl ;
    @Autowired
    public ApprenantServiceImpl(ApprenantRepository apprenantRepository,
                                UserRepository userRepository,
                                PasswordEncoder passwordEncoder,
                                RoleRepository roleRepository,
                                PromoReferentielRepository promotionReferentielRepositoryImpl

    ) {
        super(apprenantRepository);
        this.apprenantRepository = apprenantRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.promotionReferentielRepositoryImpl = promotionReferentielRepositoryImpl;
    }

    @Transactional
    public Apprenant createApprenant(ApprenantDTO apprenantDTO) {
        System.out.println("Creating Apprenant");
        try {



        // Créer l'utilisateur à partir des données du DTO
        User user = new User();
        user.setNom(apprenantDTO.getUser().getNom());
        user.setPrenom(apprenantDTO.getUser().getPrenom());
        user.setAdresse(apprenantDTO.getUser().getAdresse());
        user.setPhoto(apprenantDTO.getUser().getPhoto());
        user.setTelephone(apprenantDTO.getUser().getTelephone());
        user.setEmail(apprenantDTO.getUser().getEmail());
            Role role = roleRepository.findById(apprenantDTO.getUser().getRole_id())
                    .orElseThrow(() -> new ServiceException("Role not found"));
            user.setRole(role);

            // Hacher le mot de passe
        user.setPassword(passwordEncoder.encode(apprenantDTO.getUser().getPassword()));

        // Sauvegarder l'utilisateur
        user = userRepository.save(user);

        // Créer l'apprenant à partir des données du DTO
        Apprenant apprenant = new Apprenant();
        apprenant.setNom_tuteur(apprenantDTO.getNomTuteur());
        apprenant.setPrenom_tuteur(apprenantDTO.getPrenomTuteur());
        apprenant.setContact_tuteur(apprenantDTO.getContactTuteur());
        apprenant.setCni_file(apprenantDTO.getCniFile());
        apprenant.setExtrait_file(apprenantDTO.getExtraitFile());
        apprenant.setDiplome_file(apprenantDTO.getDiplomeFile());
        apprenant.setCasier_file(apprenantDTO.getCasierFile());
        apprenant.setPhoto_couverture(apprenantDTO.getPhotoCouverture());
        apprenant.setEtat(Apprenant.Etat.valueOf(apprenantDTO.getEtat()));
            // Récupérer l'ID du PromoReferentiel depuis le DTO
            Long promoReferentielId = apprenantDTO.getPromoReferentielId();


            PromoReferentiel promoReferentiel = promotionReferentielRepositoryImpl.findById(promoReferentielId)
                    .orElseThrow(() -> new ServiceException("PromoReferentiel not found"));
            apprenant.setPromoReferentiel(promoReferentiel);


            // Associer l'utilisateur à l'apprenant
        apprenant.setUser(user);


        // Sauvegarder l'apprenant
        return apprenantRepository.save(apprenant);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
