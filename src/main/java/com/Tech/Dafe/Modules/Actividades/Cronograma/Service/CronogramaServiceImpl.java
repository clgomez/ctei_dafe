package com.Tech.Dafe.Modules.Actividades.Cronograma.Service;

import com.Tech.Dafe.Modules.Actividades.Cronograma.Models.Cronograma;
import com.Tech.Dafe.Modules.Actividades.Cronograma.Repository.CronogramaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CronogramaServiceImpl implements CronogramaService{

    @Autowired
    private CronogramaRepository cronogramaRepository;

    public Cronograma crearCronograma(Cronograma cronograma) {
        return cronogramaRepository.save(cronograma);
    }

    public List<Cronograma> obtenerTodos() {
        return cronogramaRepository.findAll();
    }

    public Optional<Cronograma> obtenerPorId(Long id) {
        return cronogramaRepository.findById(id);
    }

    public Cronograma actualizarCronograma(Long id, Cronograma cronograma) {
        if (cronogramaRepository.existsById(id)) {
            cronograma.setId(id);
            return cronogramaRepository.save(cronograma);
        }
        return null;
    }

    public boolean eliminarCronograma(Long id) {
        if (cronogramaRepository.existsById(id)) {
            cronogramaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
