package TFG.Terranaturale.Service;

import TFG.Terranaturale.Dto.FacturaDTO;
import TFG.Terranaturale.Model.Factura;
import TFG.Terranaturale.Repository.FacturaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacturaService {
    FacturaRepository facturaRepository;
    ModelMapper modelMapper;

    public FacturaService(FacturaRepository facturaRepository, ModelMapper mapper) {
        this.facturaRepository = facturaRepository;
        this.modelMapper = mapper;
    }

    public List<FacturaDTO> findAll() {
        return facturaRepository.findAll().stream()
                .map(factura -> modelMapper.map(factura, FacturaDTO.class))
                .collect(Collectors.toList());
    }

    public FacturaDTO findById(Integer id) {
        return facturaRepository.findById(id)
                .map(factura -> modelMapper.map(factura, FacturaDTO.class))
                .orElse(null);
    }

    public FacturaDTO save(FacturaDTO facturaDTO) {
        Factura factura = modelMapper.map(facturaDTO, Factura.class);
        factura = facturaRepository.save(factura);
        return modelMapper.map(factura, FacturaDTO.class);
    }

    public void deleteById(Integer id) {
        facturaRepository.deleteById(id);
    }
}
