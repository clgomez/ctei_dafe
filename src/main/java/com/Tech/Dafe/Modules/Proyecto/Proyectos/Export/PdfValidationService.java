package com.Tech.Dafe.Modules.Proyecto.Proyectos.Export;

import com.Tech.Dafe.Exceptions.BusinessException;
import com.Tech.Dafe.Response.CampoError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PdfValidationService {

    private final FileSignatureService signatureService;

    /**
     * Valida el contenido del PDF.
     */
    public void validate(ParsedPdf pdf) {

        try {

            String signature = pdf.getSignature();

            if (signature.isBlank()) {
                throw error(
                        "El PDF no contiene la firma digital de DAFE."
                );
            }

            String payload =
                    PdfPayloadUtil.serialize(pdf.getFields());

            System.out.println("========== PAYLOAD IMPORT ==========");
            System.out.println(payload);

            System.out.println("========== SIGNATURE ==========");
            System.out.println(signature);

            System.out.println("========== GENERATED ==========");
            System.out.println(
                    signatureService.generateSignature(payload)
            );

            boolean valid =
                    signatureService.verifySignature(
                            payload,
                            signature
                    );

            if (!valid) {
                throw error(
                        "El PDF fue modificado o su firma es inválida."
                );
            }

        }
        catch (BusinessException ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw error(
                    "Error validando PDF: " + ex.getMessage()
            );
        }
    }

    private BusinessException error(String mensaje){

        List<CampoError> errores = new ArrayList<>();

        errores.add(
                new CampoError(
                        "archivo",
                        mensaje
                )
        );

        return new BusinessException(errores);

    }

}