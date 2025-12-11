package com.aiu.trips.decorator;

import com.aiu.trips.dto.TicketDTO;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * SignedQrDecorator as per Booking_Ticketing.pu diagram
 * Decorator Pattern - Adds digital signature to QR codes
 */
public class SignedQrDecorator extends TicketServiceDecorator {

    private final String secretKey;

    public SignedQrDecorator(ITicketService service) {
        super(service);
        this.secretKey = "AIU-SECRET-KEY-2024"; // In production, use environment variable
    }

    @Override
    public TicketDTO generateTicket(Long bookingId) {
        TicketDTO ticket = super.generateTicket(bookingId);
        String signedQR = signQRCode(ticket.getQrCode());
        ticket.setQrCode(signedQR);
        return ticket;
    }

    @Override
    public boolean validateQRCode(String qrCode) {
        // Validate signature before calling base validation
        if (!verifySignature(qrCode)) {
            return false;
        }
        return super.validateQRCode(qrCode);
    }

    private String signQRCode(String qrCode) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] hash = mac.doFinal(qrCode.getBytes());
            String signature = Base64.getEncoder().encodeToString(hash);
            return qrCode + "." + signature;
        } catch (Exception e) {
            return qrCode; // Fallback
        }
    }

    private boolean verifySignature(String signedQRCode) {
        if (!signedQRCode.contains(".")) {
            return false;
        }
        String[] parts = signedQRCode.split("\\.");
        if (parts.length != 2) {
            return false;
        }
        String expectedSigned = signQRCode(parts[0]);
        return expectedSigned.equals(signedQRCode);
    }
}
