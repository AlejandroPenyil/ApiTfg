package TFG.Terranaturale.Service;


import TFG.Terranaturale.Dto.SolicitudeDTO;
import TFG.Terranaturale.Exception.ResourceNotFoundException;
import TFG.Terranaturale.Model.Solicitude;
import TFG.Terranaturale.Model.Usuario;
import TFG.Terranaturale.Repository.SolicitudeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SolicitudeService {

    private final SolicitudeRepository solicitudeRepository;

    private final ModelMapper modelMapper;

    public SolicitudeService(SolicitudeRepository solicitudeRepository, ModelMapper modelMapper) {
        this.solicitudeRepository = solicitudeRepository;
        this.modelMapper = modelMapper;
    }

    public List<SolicitudeDTO> findAll() {
        return solicitudeRepository.findAll().stream()
                .map(solicitude -> modelMapper.map(solicitude, SolicitudeDTO.class))
                .collect(Collectors.toList());
    }

    public SolicitudeDTO findById(Integer id) {
        Solicitude solicitude = solicitudeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitude not found"));
        return modelMapper.map(solicitude, SolicitudeDTO.class);
    }

    public SolicitudeDTO save(SolicitudeDTO solicitudeDTO) {
        Solicitude solicitude = modelMapper.map(solicitudeDTO, Solicitude.class);
        Solicitude savedSolicitude = solicitudeRepository.save(solicitude);
        return modelMapper.map(savedSolicitude, SolicitudeDTO.class);
    }

    public SolicitudeDTO update(Integer id, SolicitudeDTO solicitudeDTO) {
        Solicitude solicitude = solicitudeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitude not found"));

        solicitude.setFechaSolicitud(solicitudeDTO.getFechaSolicitud());
        solicitude.setIdUsuario(modelMapper.map(solicitudeDTO.getIdUsuario(), Usuario.class));

        Solicitude updatedSolicitude = solicitudeRepository.save(solicitude);
        return modelMapper.map(updatedSolicitude, SolicitudeDTO.class);
    }

    public void delete(Integer id) {
        Solicitude solicitude = solicitudeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitude not found"));
        solicitudeRepository.delete(solicitude);
    }
}
