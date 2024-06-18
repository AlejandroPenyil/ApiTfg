package TFG.Terranaturale.Service;

import TFG.Terranaturale.Dto.JardineDTO;
import TFG.Terranaturale.Exception.ResourceNotFoundException;
import TFG.Terranaturale.Model.Jardine;
import TFG.Terranaturale.Model.Usuario;
import TFG.Terranaturale.Repository.JardineRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JardineService {

    private final JardineRepository jardineRepository;

    private final ModelMapper modelMapper;

    public JardineService(JardineRepository jardineRepository, ModelMapper modelMapper) {
        this.jardineRepository = jardineRepository;
        this.modelMapper = modelMapper;
    }

    public List<JardineDTO> findAll() {
        return jardineRepository.findAll().stream()
                .map(jardine -> modelMapper.map(jardine, JardineDTO.class))
                .collect(Collectors.toList());
    }

    public JardineDTO findById(Integer id) {
        Jardine jardine = jardineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Jardine not found"));
        return modelMapper.map(jardine, JardineDTO.class);
    }

    public JardineDTO save(JardineDTO jardineDTO) {
        Jardine jardine = modelMapper.map(jardineDTO, Jardine.class);
        Jardine savedJardine = jardineRepository.save(jardine);
        return modelMapper.map(savedJardine, JardineDTO.class);
    }

    public JardineDTO update(Integer id, JardineDTO jardineDTO) {
        Jardine jardine = jardineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Jardine not found"));

        jardine.setLocalizacion(jardineDTO.getLocalizacion());
        jardine.setTamaño(jardineDTO.getTamaño());
        jardine.setIdCliente(modelMapper.map(jardineDTO.getIdCliente(), Usuario.class));

        Jardine updatedJardine = jardineRepository.save(jardine);
        return modelMapper.map(updatedJardine, JardineDTO.class);
    }

    public void delete(Integer id) {
        Jardine jardine = jardineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Jardine not found"));
        jardineRepository.delete(jardine);
    }
}
