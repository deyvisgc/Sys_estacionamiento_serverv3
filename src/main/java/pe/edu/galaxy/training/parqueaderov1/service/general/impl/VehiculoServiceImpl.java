package pe.edu.galaxy.training.parqueaderov1.service.general.impl;

import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pe.edu.galaxy.training.parqueaderov1.dto.TarifaDto;
import pe.edu.galaxy.training.parqueaderov1.dto.VehiculoDto;
import pe.edu.galaxy.training.parqueaderov1.entity.*;
import pe.edu.galaxy.training.parqueaderov1.mapper.PersonaMapper;
import pe.edu.galaxy.training.parqueaderov1.mapper.TarifaMapper;
import pe.edu.galaxy.training.parqueaderov1.mapper.VehiculoMapper;
import pe.edu.galaxy.training.parqueaderov1.repository.PersonaRepository;
import pe.edu.galaxy.training.parqueaderov1.repository.TarifaRepository;
import pe.edu.galaxy.training.parqueaderov1.repository.TipoVehiculoRepository;
import pe.edu.galaxy.training.parqueaderov1.repository.VehiculoRepository;
import pe.edu.galaxy.training.parqueaderov1.service.exception.ServiceException;
import pe.edu.galaxy.training.parqueaderov1.service.exception.ServiceSqlException;
import pe.edu.galaxy.training.parqueaderov1.service.general.service.VehiculoService;
import pe.edu.galaxy.training.parqueaderov1.utils.Constantes;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.lowagie.text.*;

import static java.util.Objects.isNull;
@Service
public class VehiculoServiceImpl implements VehiculoService {
    @Autowired
    private VehiculoRepository vehiculoRepository;
    @Autowired
    private VehiculoMapper  vehiculoMapper;
    @Autowired
    private TarifaRepository tarifaRepository;
    @Autowired
    private TarifaMapper tarifaMapper;
    @Autowired
    TipoVehiculoRepository tipoVehiculoRepository;
    @Autowired
    private PersonaRepository personaRepository;
    @Autowired
    private PersonaMapper personaMapper;
    @Override
    public List<VehiculoDto> findAll() throws ServiceException, ServiceSqlException {
        try {
            List<VehiculoEntity> lsVehiculo = vehiculoRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

            return lsVehiculo.stream()
                    .filter(ls-> ls.getStatusDelete() == '1')
                    .map(ls -> vehiculoMapper.toVehiculoDto(ls)).collect(Collectors.toList());
        } catch (RuntimeException e) {
            throw new ServiceException(e);
        }

    }
    @Override
    public Optional<VehiculoDto> findById(long id) throws ServiceException {
        try {
            return vehiculoRepository.findById(id)
                    .map( v -> vehiculoMapper.toVehiculoDto(v));
        } catch (RuntimeException e) {
            throw new ServiceException(e);
        }
    }
    @Override
    public Optional<VehiculoDto> findByCodigoOperacionAndEstado(String codigo, char estado) throws ServiceException {
        try {
            return vehiculoRepository.findByCodigoOperacionAndEstado(codigo, estado)
                    .map(o -> vehiculoMapper.toVehiculoDto(o));
        } catch (RuntimeException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Optional<VehiculoDto> findByCodigoOperacion(String codigo) throws ServiceException {
        try {
            return vehiculoRepository.findByCodigoOperacion(codigo)
                    .map(o -> vehiculoMapper.toVehiculoDto(o));
        } catch (RuntimeException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean existsByCedula(String cedula) throws ServiceException {
        try {
            return vehiculoRepository.existsByCedula(cedula);
        } catch (RuntimeException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void ingreso(VehiculoDto vehiculoDto) throws ServiceException {
        try {
                vehiculoRepository.ingresoVehiculo(
                    vehiculoDto.getLicense_plate(),
                    vehiculoDto.getCedula(),
                   (int) vehiculoDto.getType_vehicle().getId(),
                        vehiculoDto.getCheck_in_time(),
                    vehiculoDto.getPerson().getName(),
                    vehiculoDto.getPerson().getPhone(),
                    vehiculoDto.getPerson().getNumber(),
                    vehiculoDto.getPerson().getAddres());
        } catch (RuntimeException e) {
            throw new ServiceException(e);
        }
    }
    @Transactional(Transactional.TxType.REQUIRED)
    @Override
    public TarifaDto salida(VehiculoDto vehiculoDto) throws ServiceException {
        try {
            LocalTime horaEntrada = LocalTime.parse(Constantes.formatTime(vehiculoDto.getCheck_in_time()));
            LocalTime horaSalida = LocalTime.parse(Constantes.formatTime(LocalTime.now()));
            Double precioXHora = vehiculoDto.getType_vehicle().getPrecioHora();
            long seconds = Math.abs(ChronoUnit.SECONDS.between(horaEntrada, horaSalida));
            String hms = String.format("%02d:%02d:%02d", seconds / Constantes.TOTALSEGUNDOSXHORA, (seconds / Constantes.TOTALMINUTOSXHORA) % Constantes.TOTALMINUTOSXHORA, seconds % Constantes.TOTALMINUTOSXHORA);
            String totalHoras = "";
            if (LocalTime.parse(hms).getMinute() > Constantes.MEDIAHORA) {
                totalHoras = String.valueOf(LocalTime.parse(hms).plusHours(1));
            } else {
                totalHoras = hms;
            }
            BigDecimal montoXminuto = new BigDecimal((double) precioXHora /Constantes.TOTALMINUTOSXHORA).setScale(6, RoundingMode.HALF_UP);
            Integer tHoras = Integer.valueOf(totalHoras.substring(0, 2)) * Constantes.TOTALMINUTOSXHORA;
            double montoPagar = tHoras * montoXminuto.doubleValue();
        /*System.out.println("hms: " +  hms);
         System.out.println("montoXminuto.doubleValue()" + montoXminuto.doubleValue());
        System.out.println("totalHoras:" +  totalHoras);
        System.out.println("tHoras: " + tHoras);
        System.out.println("montoPagar: " +  (double) Math.round(montoPagar));
         */
            vehiculoRepository.salida(horaSalida, new Date(), vehiculoDto.getId());
            TarifaDto tarifaDto = cobrar(vehiculoDto, (double) Math.round(montoPagar), Integer.valueOf(totalHoras.substring(0, 2)));
            return tarifaDto;
        }catch (RuntimeException e) {
            System.err.println("e" + e.getMessage());
            throw new ServiceException(e);
        }

    }
    @Transactional(Transactional.TxType.REQUIRED)
    public TarifaDto cobrar(VehiculoDto  vehiculoDto, Double montoPagar, int TotalHoras) {
        String codigoTarifa = tarifaRepository.obtenerCodigoTarifa();
        if (isNull(codigoTarifa)) {
            codigoTarifa = "CT0001";
        }
        BigDecimal montotTotal = new BigDecimal((double) montoPagar).setScale(2, RoundingMode.HALF_UP);
        TarifaDto tarifaDto = TarifaDto.builder()
                .vehiculo(vehiculoDto)
                .code(codigoTarifa)
                .totalHour(TotalHoras)
                .total(montotTotal.doubleValue())
                .build();
        TarifaEntity entity = tarifaRepository.save(tarifaMapper.toTarifaEntity(tarifaDto));
        return tarifaMapper.toTarifaDto(entity);
    }
    @Transactional(Transactional.TxType.REQUIRED)
    @Override
    public void export(HttpServletResponse response, long idVehiculo) throws IOException {
        try{
            System.out.println("entro");
            Rectangle pagesize = new Rectangle(225f, 400f);  // x => ancho, y => alto
            Document document = new Document(pagesize);
            PdfWriter.getInstance(document,response.getOutputStream());
            document.open();
            Optional<VehiculoEntity> vehiculo = vehiculoRepository.findById(idVehiculo);
            if (vehiculo.isEmpty()) {
                throw new IOException("vehiculo no existe");
            }
            TarifaEntity tarifa = tarifaRepository.findByVehiculoEntity(vehiculo.get());
            if (isNull(tarifa)) {
                throw new IOException("tarifa no crada");
            }
            Optional<TipoVehiculoEntity> tipoVehiculo = tipoVehiculoRepository.findById(vehiculo.get().getTipoVehiculo().getId());
            if (tipoVehiculo.isEmpty()) {
                throw new IOException("tipo vehiculo no existe");
            }
            String fileName = "D:\\Proyectos Deyvis\\Curgo Galaxy\\proyecto galaxy\\Sys_estacionamiento_server\\src\\main\\java\\pe\\edu\\galaxy\\training\\parqueaderov1\\img\\image.jpg";
            //InputStream input = new FileInputStream(fileName);
            //byte[] bytes = IOUtils.readFully(input);
            // InputStreamResource inputStreamReader = new InputStreamResource(new FileInputStream(path));
            // System.out.println("inputStreamReader.getFilename(" + inputStreamReader.getFilename());
        /* BufferedImage bufferedImage = ImageIO.read(new File(fileName));
        ByteOutputStream bos = new ByteOutputStream();
         ImageIO.write(bufferedImage, "jpg", bos);
         Image image = Image.getInstance(400, 100, bos.getBytes(), null);
         image.setIndentationLeft(10);
         image.setDpi(20, 30);
         document.add(image);

         */
            Font fontForTitle = FontFactory.getFont(FontFactory.COURIER);
            fontForTitle.setSize(14);
            Paragraph title = new Paragraph("Boleta de pago", fontForTitle);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            title.setSpacingAfter(2);
            Paragraph empresa = new Paragraph("PLAYA DE ESTABLECIMIENTO I.H.G.C", FontFactory.getFont(FontFactory.COURIER, 8));
            empresa.setAlignment(Paragraph.ALIGN_CENTER);
            Font fontForKeys = FontFactory.getFont(FontFactory.COURIER);
            fontForKeys.setSize(10);
            empresa.setSpacingAfter(10);

            Paragraph codigoTarifa = new Paragraph("Codigo: ", fontForKeys);
            codigoTarifa.add(new Chunk(tarifa.getCodigo(), FontFactory.getFont(FontFactory.COURIER, 10)));
            codigoTarifa.setAlignment(Paragraph.ALIGN_LEFT);
            codigoTarifa.setSpacingAfter(1);

            Paragraph dni = new Paragraph("DNI: ", fontForKeys);
            dni.add(new Phrase(vehiculo.get().getPersonaVehiculo().getNumeroDocumento(), FontFactory.getFont(FontFactory.COURIER, 10)));
            dni.setAlignment(Paragraph.ALIGN_LEFT);
            dni.setSpacingAfter(1);

            Paragraph placa = new Paragraph("Placa: ", fontForKeys);
            placa.add(new Chunk(vehiculo.get().getPlaca(), FontFactory.getFont(FontFactory.COURIER, 10)));
            placa.setAlignment(Paragraph.ALIGN_LEFT);
            placa.setSpacingAfter(1);
            ZoneId zoneId = ZoneId.of("America/Lima");

            String fechaEntrada = Constantes.formatDateTime(vehiculo.get().getFechaRegistro().toInstant().atZone(zoneId).toLocalDateTime()); // convertir a formato fecha
            String fechaSalida = Constantes.formatDateTime(tarifa.getVehiculoEntity().getFechaSalida().toInstant().atZone(zoneId).toLocalDateTime());

            Paragraph entrada = new Paragraph("Entrada: ", fontForKeys);
            entrada.add(new Chunk(fechaEntrada, FontFactory.getFont(FontFactory.COURIER, 10)));
            entrada.setAlignment(Paragraph.ALIGN_LEFT);
            entrada.setSpacingAfter(1);

            Paragraph salida = new Paragraph("Salida: ", fontForKeys);
            salida.add(new Chunk(fechaSalida, FontFactory.getFont(FontFactory.COURIER, 10)));
            salida.setAlignment(Paragraph.ALIGN_LEFT);
            salida.setSpacingAfter(1);

            Paragraph totalHoras = new Paragraph("Total Horas: ", fontForKeys);
            totalHoras.add(new Chunk(String.valueOf(tarifa.getTotalHoras()), FontFactory.getFont(FontFactory.COURIER, 10)));
            totalHoras.setAlignment(Paragraph.ALIGN_LEFT);
            totalHoras.setSpacingAfter(1);
            Paragraph precioHora = new Paragraph("Precio Por Hora: ", fontForKeys);
            precioHora.add(new Chunk(String.valueOf(tipoVehiculo.get().getPrice_hour()), FontFactory.getFont(FontFactory.COURIER, 10)));
            precioHora.setAlignment(Paragraph.ALIGN_LEFT);
            precioHora.setSpacingAfter(1);

            Paragraph totalPagar = new Paragraph("Total Pagar: ", fontForKeys);
            totalPagar.add(new Chunk("S/ " + String.valueOf(tarifa.getMontoPagar()), FontFactory.getFont(FontFactory.COURIER, 10)));
            totalPagar.setAlignment(Paragraph.ALIGN_LEFT);
            totalPagar.setSpacingAfter(50);

            Paragraph footer = new Paragraph("!Gracias por confiar en nosotros!", fontForKeys);
            footer.setAlignment(Paragraph.ALIGN_CENTER);

            document.add(title);
            document.add(empresa);
            document.add(codigoTarifa);
            document.add(dni);
            document.add(placa);
            document.add(entrada);
            document.add(salida);
            document.add(totalHoras);
            document.add(precioHora);
            document.add(totalPagar);
            document.add(footer);
            //document.add(telefono);
            document.close();
        } catch (RuntimeException e) {
            throw new IOException(e);
        }
        //document.add(nombre);
        //document.add(tabla);

        /*
        PdfPTable tabla = new PdfPTable(5);
        PdfPCell cell;
        cell = new PdfPCell(new Phrase("Cantidad"));
        cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5);
        cell.setBorder(0);
        cell.setBorderWidthTop(1);
        cell.setBorderWidthBottom(1);
        tabla.addCell(cell);
        cell = new PdfPCell(new Phrase("Unidad"));
        cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5);
        cell.setBorder(0);
        cell.setBorderWidthTop(1);
        cell.setBorderWidthBottom(1);
        tabla.addCell(cell);

        cell = new PdfPCell(new Phrase("Descripcion"));
        cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5);
        cell.setBorder(0);
        cell.setBorderWidthTop(1);
        cell.setBorderWidthBottom(1);
        tabla.addCell(cell);

        cell = new PdfPCell(new Phrase("Precio"));
        cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5);
        cell.setBorder(0);
        cell.setBorderWidthTop(1);
        cell.setBorderWidthBottom(1);
        tabla.addCell(cell);
        cell = new PdfPCell(new Phrase("Subtotal"));
        cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5);
        cell.setBorder(0);
        cell.setBorderWidthTop(1);
        cell.setBorderWidthBottom(1);
        cell.setSpaceCharRatio(10);
        tabla.addCell(cell);


        /// datos de la tarifa
        cell = new PdfPCell(new Phrase("1"));
        cell.setPadding(5);
        cell.setBorder(0);
        tabla.addCell(cell);

        cell = new PdfPCell(new Phrase("1"));
        cell.setPadding(5);
        cell.setBorder(0);
        tabla.addCell(cell);

        cell = new PdfPCell(new Phrase("1"));
        cell.setPadding(5);
        cell.setBorder(0);
        tabla.addCell(cell);
        cell = new PdfPCell(new Phrase("1"));

        cell.setPadding(5);
        cell.setBorder(0);
        tabla.addCell(cell);

        cell = new PdfPCell(new Phrase("1"));
        cell.setPadding(5);
        cell.setBorder(0);
        tabla.addCell(cell);


        /*
        celda = new PdfPCell(new Phrase(cliente.getNombres(), fuenteDataCeldas));
        celda.setPadding(5);
        tablaClientes.addCell(celda);

        celda = new PdfPCell(new Phrase(cliente.getApellidos(), fuenteDataCeldas));
        celda.setPadding(5);
        tablaClientes.addCell(celda);

        celda = new PdfPCell(new Phrase(cliente.getTelefono(), fuenteDataCeldas));
        celda.setPadding(5);
        tablaClientes.addCell(celda);

        celda = new PdfPCell(new Phrase(cliente.getEmail(), fuenteDataCeldas));
        celda.setPadding(5);
        tablaClientes.addCell(celda);

        celda = new PdfPCell(new Phrase(cliente.getCiudad().getCiudad(), fuenteDataCeldas));
        celda.setPadding(5);

         */
    }

    @Override
    public List<VehiculoDto> findByObject(VehiculoDto vehiculoDto) throws ServiceException {
        return null;
    }
    @Transactional(Transactional.TxType.REQUIRED)
    @Override
    public VehiculoDto save(VehiculoDto vehiculoDto) throws ServiceException{
        return null;
    }

    @Override
    public VehiculoDto update(VehiculoDto vehiculoDto) throws ServiceException {
        try {
            Optional<VehiculoEntity> vehiculo = vehiculoRepository.findByPlaca(vehiculoDto.getLicense_plate());
            boolean edit = false;
            if (vehiculo.isEmpty()) {
                edit = true;
            } else if(vehiculo.isPresent() && vehiculo.get().getId() == vehiculoDto.getId()) {
                edit = true;
            }
            if (edit) {
                vehiculoDto.getPerson().setId(vehiculo.get().getPersonaVehiculo().getId());
                PersonaEntity refPersona = personaMapper.toPersonaEntity(vehiculoDto.getPerson());
                BeanUtils.copyProperties(vehiculoDto.getPerson(), refPersona);
                PersonaEntity persona = personaRepository.save(refPersona);
                System.out.println("persona" + persona.toString());

                VehiculoEntity refVehiculo = vehiculoMapper.toVehiculoEntity(vehiculoDto);
                BeanUtils.copyProperties(vehiculoDto , refVehiculo);
                Optional<TipoVehiculoEntity> tipoVehiculo = tipoVehiculoRepository.findById(vehiculoDto.getType_vehicle().getId());
                refVehiculo.setPersonaVehiculo(persona);
                refVehiculo.setTipoVehiculo(tipoVehiculo.get());
                refVehiculo.setEstado('1');
                refVehiculo.setStatusDelete('1');
                VehiculoEntity vehiculoDto1 = vehiculoRepository.save(refVehiculo);
                return vehiculoMapper.toVehiculoDto(vehiculoDto1);
            } else {
                return null;
            }
        }catch (RuntimeException e) {
            System.out.println("e"+ e);
            throw new ServiceException(e);
        }
    }

    @Override
    public void deleteLogic(Long id) throws ServiceException {
        try {
            vehiculoRepository.deleteLogic(id);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }




}
