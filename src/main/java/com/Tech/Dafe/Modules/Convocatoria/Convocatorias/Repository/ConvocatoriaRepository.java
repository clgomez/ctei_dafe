package com.Tech.Dafe.Modules.Convocatoria.Convocatorias.Repository;

import com.Tech.Dafe.Modules.Convocatoria.Convocatorias.Models.Convocatoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConvocatoriaRepository extends JpaRepository<Convocatoria, Long> {

    boolean existsByTituloNormalizado(String tituloNormalizado);
    boolean existsByTituloNormalizadoAndIdNot(String tituloNormalizado, Long id);
}
