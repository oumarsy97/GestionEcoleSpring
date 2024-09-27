package sn.odc.oumar.springproject.Web.Controller.Interfaces;

import org.springframework.http.ResponseEntity;
import sn.odc.oumar.springproject.Datas.Entity.Apprenant;
import sn.odc.oumar.springproject.Web.Dtos.Request.ApprenantDTO;

public interface ApprenantControllerInterface  {
    public ResponseEntity<Apprenant> ajouterApprenant(ApprenantDTO apprenant);

}
