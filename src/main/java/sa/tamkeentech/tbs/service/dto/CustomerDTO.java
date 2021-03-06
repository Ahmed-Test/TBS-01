package sa.tamkeentech.tbs.service.dto;
import java.io.Serializable;
import java.util.Objects;

import io.swagger.annotations.ApiModel;
import lombok.*;
import sa.tamkeentech.tbs.domain.enumeration.IdentityType;

/**
 * A DTO for the {@link sa.tamkeentech.tbs.domain.Customer} entity.
 */
@ApiModel(description = "The Custom entity.")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class CustomerDTO implements Serializable {

    private Long id;

    private String identity;

    private IdentityType identityType;

    private String name;

    private String email;

    private String phone;

}
