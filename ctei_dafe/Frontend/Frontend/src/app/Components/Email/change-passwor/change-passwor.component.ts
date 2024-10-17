import { ToastrService } from 'ngx-toastr';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { EmailPasswordService } from '../../../Services/email.service';
import { ChangePasswordDTO } from '../models/change-password-dto';

@Component({
  selector: 'app-change-passwor',
  templateUrl: './change-passwor.component.html',
  styleUrls: ['./change-passwor.component.css'],
})
export class ChangePassworComponent implements OnInit {
  password: string = '';
  confirmPassword: string = '';
  tokenPassword: string = '';

  constructor(
    private emailPasswordService: EmailPasswordService,
    private toastrService: ToastrService,
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) {}

  ngOnInit() {
    this.activatedRoute.paramMap.subscribe((params: ParamMap) => {
      this.tokenPassword = params.get('tokenPassword') || '';
    });
  }
  onChangePassword(): void {
    if (this.password !== this.confirmPassword) {
      alert('Las contraseñas no coinciden');
      return;
    }

    const dto = new ChangePasswordDTO(
      this.password,
      this.confirmPassword,
      this.tokenPassword
    );

    this.emailPasswordService.changePassword(dto).subscribe({
      next: (data) => {
        alert('Cambio exitoso, puedes inicia sesión');
        this.router.navigate(['/login']);
      },
      error: (err) => {
        alert(err.error.mensaje);
      },
    });
  }
}
