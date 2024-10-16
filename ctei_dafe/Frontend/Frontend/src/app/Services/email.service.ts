import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../Environments/environment';
import { EmailValuesDTO } from '../Components/Email/models/email-values-dto';
import { ChangePasswordDTO } from '../Components/Email/models/change-password-dto';


@Injectable({
  providedIn: 'root',
})
export class EmailPasswordService {
  private changePasswordURL = `${environment.apiUrl}/email`;

  constructor(private httpClient: HttpClient) {}

  public sendEmail(dto: EmailValuesDTO): Observable<any> {
    return this.httpClient.post<any>(
      this.changePasswordURL + '/send-email-password',
      dto
    );
  }

  public changePassword(dto: ChangePasswordDTO): Observable<any> {
    return this.httpClient.post<any>(
      this.changePasswordURL + '/change-password',
      dto
    );
  }
}
