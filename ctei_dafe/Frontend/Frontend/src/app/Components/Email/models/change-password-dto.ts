export class ChangePasswordDTO {
  constructor(
    public password: string,
    public confirmarPassword: string,
    public tokenPassword: string
  ) {}
}
