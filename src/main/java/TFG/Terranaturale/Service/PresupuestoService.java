package TFG.Terranaturale.Service;

import TFG.Terranaturale.Dto.PresupuestoDTO;
import TFG.Terranaturale.Dto.UsuarioDTO;
import TFG.Terranaturale.Exception.ResourceNotFoundException;
import TFG.Terranaturale.Model.Presupuesto;
import TFG.Terranaturale.Model.Solicitude;
import TFG.Terranaturale.Model.Usuario;
import TFG.Terranaturale.Repository.JardineRepository;
import TFG.Terranaturale.Repository.PresupuestoRepository;
import TFG.Terranaturale.Repository.SolicitudeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PresupuestoService {

    private final PresupuestoRepository presupuestoRepository;
    private final SolicitudeRepository solicitudeRepository;
    private final JardineRepository jardineRepository;

    private final ModelMapper modelMapper;

    public PresupuestoService(PresupuestoRepository presupuestoRepository, SolicitudeRepository solicitudeRepository, JardineRepository jardineRepository, ModelMapper modelMapper) {
        this.presupuestoRepository = presupuestoRepository;
        this.solicitudeRepository = solicitudeRepository;
        this.jardineRepository = jardineRepository;
        this.modelMapper = modelMapper;
    }

    public List<PresupuestoDTO> findAll() {
        return presupuestoRepository.findAll().stream()
                .map(presupuesto -> modelMapper.map(presupuesto, PresupuestoDTO.class))
                .collect(Collectors.toList());
    }

    public PresupuestoDTO findById(Integer id) {
        Presupuesto presupuesto = presupuestoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Presupuesto not found"));
        return modelMapper.map(presupuesto, PresupuestoDTO.class);
    }

    public PresupuestoDTO save(PresupuestoDTO presupuestoDTO) {
        Presupuesto presupuesto = modelMapper.map(presupuestoDTO, Presupuesto.class);
        Presupuesto savedPresupuesto = presupuestoRepository.save(presupuesto);
        return modelMapper.map(savedPresupuesto, PresupuestoDTO.class);
    }

    public PresupuestoDTO update(Integer id, PresupuestoDTO presupuestoDTO) {
        Presupuesto presupuesto = presupuestoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Presupuesto not found"));

        presupuesto.setFechalEnvio(presupuestoDTO.getFechalEnvio());
        presupuesto.setFechaAceptado(presupuestoDTO.getFechaAceptado());
        presupuesto.setEstado(presupuestoDTO.getEstado());
        presupuesto.setIdJardin(jardineRepository.findById(presupuestoDTO.getIdJardin()).orElseThrow(()-> new ResourceNotFoundException("Presupuesto not found")));
        presupuesto.setUbicacion(presupuestoDTO.getUbicacion());

        Presupuesto updatedPresupuesto = presupuestoRepository.save(presupuesto);
        return modelMapper.map(updatedPresupuesto, PresupuestoDTO.class);
    }

    public void delete(Integer id) {
        Presupuesto presupuesto = presupuestoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Presupuesto not found"));
        presupuestoRepository.delete(presupuesto);
    }

    public ResponseEntity<List<PresupuestoDTO>> findByClient(UsuarioDTO id) {
        Usuario usuario = modelMapper.map(id, Usuario.class);
        List<Solicitude> solicitudeDTOS = solicitudeRepository.findByIdUsuario(usuario);

        List<Presupuesto> presupuestoList = new ArrayList<>();

        for(Solicitude solicitudeDTO : solicitudeDTOS) {
            List<Presupuesto> presupuestos = presupuestoRepository.findByIdSolicitud(solicitudeDTO);
            presupuestoList.addAll(presupuestos);
        }

        List<PresupuestoDTO> presupuestoDTOList = new ArrayList<>();
        for (Presupuesto presupuesto : presupuestoList) {
            presupuestoDTOList.add(modelMapper.map(presupuesto, PresupuestoDTO.class));
        }
        return ResponseEntity.ok(presupuestoDTOList);
    }

    public ResponseEntity<InputStreamResource> download(Integer id) throws IOException {
        Presupuesto presupuesto = presupuestoRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Presupuesto not found"));
        File file = new File(presupuesto.getUbicacion());
        if (file.exists()){
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(file.length())
                    .body(resource);
        }else{
            return ResponseEntity.notFound().build();
        }
    }
}
