package sn.odc.oumar.springproject.Web.Dtos.Request;

import lombok.Getter;
import lombok.Setter;
import sn.odc.oumar.springproject.Datas.Entity.BaseEntity;

@Getter
@Setter
public class RoleDTO extends BaseEntity {
    private String libelle;
}
