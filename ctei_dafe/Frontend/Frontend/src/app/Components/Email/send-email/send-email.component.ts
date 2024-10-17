import { Component, OnInit } from '@angular/core';
import { EmailValuesDTO } from './../models/email-values-dto';
import { EmailPasswordService } from '../../../Services/email.service';

@Component({
  selector: 'app-send-email',
  templateUrl: './send-email.component.html',
  styleUrls: ['./send-email.component.css'],
})
export class SendEmailComponent implements OnInit {
  mailTo: string = '';
  dto: EmailValuesDTO = new EmailValuesDTO('');
  message: string = '';
  isError: boolean = false;

  constructor(private emailPasswordService: EmailPasswordService) {}

  ngOnInit() {}

  onSendEmail(): void {
    this.dto = new EmailValuesDTO(this.mailTo);
    this.emailPasswordService.sendEmail(this.dto).subscribe(
      (data) => {
        this.message = data.mensaje;
        this.isError = false;
      },
      (err) => {
        this.message =
          err.error.mensaje || 'Ocurrió un error al enviar el correo';
        this.isError = true;
      }
    );
  }
}
