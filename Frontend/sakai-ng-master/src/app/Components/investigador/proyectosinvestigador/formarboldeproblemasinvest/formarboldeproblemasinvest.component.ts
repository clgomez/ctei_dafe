import { Component, OnDestroy, OnInit } from '@angular/core';
import { Proyecto } from '@app/Models/proyecto.model';
import { ProyectoService } from '@app/Services/proyecto.service';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '@app/Services/auth.service';
import { User } from '@app/Models/user.model'; 
import { ArbolDeProblemas } from '@app/Models/arboldeproblemas.model';
import { ArbolDeProblemasService } from '@app/Services/arboldeproblemas.service';
import { Causa } from '@app/Models/causa.model';
import { CausaService } from '@app/Services/causa.service';
import { FormBuilder, FormGroup, FormArray } from '@angular/forms';
import { Efecto } from '@app/Models/efecto.model';
import { EfectoService } from '@app/Services/efecto.service';
import { firstValueFrom } from 'rxjs';
import { Validators } from '@angular/forms';
import swal from 'sweetalert2';


@Component({
  selector: 'app-formarboldeproblemasinvest',
  templateUrl: './formarboldeproblemasinvest.component.html',
  styleUrls: ['./formarboldeproblemasinvest.component.css']


})

export class FormArbolDeProblemasInvestComponent implements OnInit,OnDestroy{

  currentUser: User | null = null;

  public proyecto: Proyecto = new Proyecto();

  public tituloArbolDeProblemas: string = 'Árbol de Problemas';

  public tituloCausasArbolDeProblemas: string = 'Causas del Árbol de Problemas';

  public tituloEfectosArbolDeProblemas: string = 'Efectos del Árbol de Problemas';

  public arbolDeProblemas: ArbolDeProblemas = new ArbolDeProblemas();

  public causa: Causa = new Causa();

  public causasArbolDeProblemas: Causa[] = [];

  public efecto: Efecto = new Efecto();

  public efectosArbolDeProblemas: Efecto[] = [];

  formularioCausas: FormGroup;

  formularioEfectos: FormGroup;

  formularioArbol: FormGroup;

  constructor(private proyectoService: ProyectoService,
              private arbolDeProblemasService: ArbolDeProblemasService,
              private causaService: CausaService,
              private efectoService: EfectoService,
              private router: Router, private authService: AuthService,
              private activatedRoute: ActivatedRoute,
              private fb: FormBuilder){
                /*this.formularioCausas = this.fb.group({
                    causas: this.fb.array([])
                  });
                  this.formularioEfectos = this.fb.group({
                    efectos: this.fb.array([])
                  });
                */
                  this.formularioArbol = this.fb.group({
                    descripcion: ['', [Validators.required, Validators.minLength(10)]],
                    causas: this.fb.array([]),
                    efectos: this.fb.array([])
                  },{
                      validators: [this.validarCantidadCausasEfectos]  // validación personalizada
                    });  
                                }

  ngOnInit(): void {

   this.authService.currentUser.subscribe(user => this.currentUser = user);

   this.cargarProyecto();
   this.cargarArbolDeProblemas(); 
  

  }


  /*get causas(): FormArray {
    return this.formularioCausas.get('causas') as FormArray;
  }*/
  
  get causas(): FormArray {
    return this.formularioArbol.get('causas') as FormArray;
  }

  agregarCausa(): void {
    this.causas.push(this.fb.group({
      id: [null], // se quedará null hasta que el backend devuelva uno
      descripcion: ['', [Validators.required, Validators.minLength(5)]], // Inicializa el campo con un string vacío
      esExistente: [false] // nuevo input
    }));
  }

  private crearCausaFormGroup(causa: Causa): FormGroup {
  return this.fb.group({
    id: [causa.id],
    descripcion: [causa.descripcion || '', [Validators.required, Validators.minLength(5)]],
    esExistente: [!!causa.id] // true si ya viene de backend
  });
}


  /*get efectos(): FormArray {
    return this.formularioEfectos.get('efectos') as FormArray;
  }*/
  
  get efectos(): FormArray {
    return this.formularioArbol.get('efectos') as FormArray;
  }

  agregarEfecto(): void {
    this.efectos.push(this.fb.group({
      id: [null], // se quedará null hasta que el backend devuelva uno
      descripcion: ['', [Validators.required, Validators.minLength(5)]],  // Inicializa el campo con un string vacío
      esExistente: [false] // nuevo input
    }));
  }

  private crearEfectoFormGroup(efecto: Efecto): FormGroup {
  return this.fb.group({
    id: [efecto.id],
    descripcion: [efecto.descripcion || '', [Validators.required, Validators.minLength(5)]],
    esExistente: [!!efecto.id] // true si ya viene de backend
  });
}

// Validador personalizado
validarCantidadCausasEfectos(formGroup: FormGroup) {
  const causas = formGroup.get('causas') as FormArray;
  const efectos = formGroup.get('efectos') as FormArray;

  if (!causas || !efectos) return null;

  // Si el número de causas es igual al número de efectos, es válido
  return causas.length === efectos.length
    ? null
    : { cantidadNoCoincide: true };
}

  get formularioValido(): boolean {
   
    const descripcionValida = this.formularioArbol.get('descripcion')?.valid;

    const causasValidas = this.causas.length > 0 && this.causas.controls.every(c => c.valid);
    const efectosValidos = this.efectos.length > 0 && this.efectos.controls.every(e => e.valid);

    const cantidadesIguales = !this.formularioArbol.hasError('cantidadNoCoincide');

    return !!(descripcionValida && causasValidas && efectosValidos && cantidadesIguales);
 }

 // Quitar una causa específica
quitarCausa(index: number) {
  this.causas.removeAt(index);
}

 // Quitar la última causa
/* quitarUltimaCausa() {
  if (this.causas.length > 0) {
    this.causas.removeAt(this.causas.length - 1);
  }
}
*/

// Quitar la última causa (solo si NO viene del backend)
quitarUltimaCausa() {
  const causasArray = this.causas;
  if (causasArray.length > 0) {
    // Buscar la última causa que no sea existente
    for (let i = causasArray.length - 1; i >= 0; i--) {
      const causa = causasArray.at(i);
      if (!causa.get('esExistente')?.value) {
        causasArray.removeAt(i);
        break; // elimina solo una
      }
    }
  }
}

// Quitar un efecto específico
quitarEfecto(index: number) {
  this.efectos.removeAt(index);
}

// Quitar el último efecto
/*quitarUltimoEfecto() {
  if (this.efectos.length > 0) {
    this.efectos.removeAt(this.efectos.length - 1);
  }
}
*/

// Quitar el último efecto (solo si NO viene del backend)
quitarUltimoEfecto() {
  const efectosArray = this.efectos;
  if (efectosArray.length > 0) {
    // Buscar el último efecto que no sea existente
    for (let i = efectosArray.length - 1; i >= 0; i--) {
      const efecto = efectosArray.at(i);
      if (!efecto.get('esExistente')?.value) {
        efectosArray.removeAt(i);
        break;
      }
    }
  }
}

marcarCausaParaEliminar(index: number): void {
  const causaControl = this.causas.at(index);
  if (causaControl.get('esExistente')?.value) {
    //causaControl.disable(); // desactiva el campo
    console.log('id causa',causaControl.value.id);
    (causaControl as any).marcadaParaEliminar = true; // marca internamente
    this.eliminarCausa(causaControl.value.id);
  }
}

marcarEfectoParaEliminar(index: number): void {
  const efectoControl = this.efectos.at(index);
  if (efectoControl.get('esExistente')?.value) {
    //efectoControl.disable();
    console.log('id efecto',efectoControl.value.id);
    (efectoControl as any).marcadaParaEliminar = true;
    this.eliminarEfecto(efectoControl.value.id);
  }
}


  cargarProyecto(): void{
    this.activatedRoute.params.subscribe(params => {
      let id = params['idproyecto']
      if(id){
        this.proyectoService.getProyecto(id).subscribe( (proyecto) =>
          {this.proyecto = proyecto;
            console.log('id del proyecto ok:', this.proyecto);
            //this.cargarArbolDeProblemas();
          })

      }
    })
  }


   cargarArbolDeProblemas(): void{
    this.activatedRoute.params.subscribe(params => {
      let id = params['idarbol']
      if(id){
        this.arbolDeProblemasService.getArbolDeProblemas(id).subscribe( 
        { next: (arbolDeProblemas) =>{ 
            this.arbolDeProblemas = arbolDeProblemas;
            console.log('descripcion del arbol de problemas ok:', this.arbolDeProblemas.descripcion);
            // Actualizar valores en el formulario
            this.formularioArbol.patchValue({
              descripcion: this.arbolDeProblemas.descripcion
            });
            console.log('id del arbol de problemas ok:', this.arbolDeProblemas);
            this.cargarCausas();
            this.cargarEfectos();
         },
         error: (e) =>{ 
            console.error('Error al cargar árbol de problemas:', e);
          }
            
          });
      }else
      {
        console.log("El árbol de problemas no existe para este proyecto.");
      }
    })
  }
   

  cargarCausas():void
    {
      this.causaService.getCausasPorIdArbolDeProblemas(this.arbolDeProblemas.id).subscribe({
        next: (causas) => {
              this.causasArbolDeProblemas =  Array.isArray(causas) ? causas : [];
              const causasArray = this.formularioArbol.get('causas') as FormArray;
              causasArray.clear(); // Limpia el FormArray antes de llenarlo

              this.causasArbolDeProblemas.forEach(causa => {
                causasArray.push(this.crearCausaFormGroup(causa));
              });
  
            if(this.causasArbolDeProblemas.length == 0)
               //swal.fire('lista vacia', `${respose.mensaje}:`, 'success')
               console.log('lista vacia');
  
  
        },
        error: (e) => {
          console.error('Error al cargar causas:', e);
  
        }
      });
  
    }

   cargarEfectos():void
    {
      this.efectoService.getEfectosPorIdArbolDeProblemas(this.arbolDeProblemas.id).subscribe({
        next: (efectos) => {
              this.efectosArbolDeProblemas =  Array.isArray(efectos) ? efectos : [];
              const efectosArray = this.formularioArbol.get('efectos') as FormArray;
              efectosArray.clear(); // Limpia el FormArray antes de llenarlo

              this.efectosArbolDeProblemas.forEach(efecto => {
                efectosArray.push(this.crearEfectoFormGroup(efecto));
              });
  
            if(this.efectosArbolDeProblemas.length == 0)
               //swal.fire('lista vacia', `${respose.mensaje}:`, 'success')
               console.log('lista vacia');
  
  
        },
        error: (e) => {
          console.error('Error al cargar efectos:', e);
  
        }
      });
  
    } 

   public async crearArbolDeProblemas():  Promise<void> {

    try{

      const formulario = this.formularioArbol.value;

      // Asignar los valores del formulario al objeto
      this.arbolDeProblemas.descripcion = formulario.descripcion;
      this.arbolDeProblemas.proyectoId = this.proyecto.id;

      // Crear el árbol de problemas primero
      const arbolCreado: any = await firstValueFrom(
        this.arbolDeProblemasService.createArbolDeProblemas(this.arbolDeProblemas)
      );
      
          //this.arbolDeProblemas.id = arbolCreado.id;
          this.arbolDeProblemas.id = arbolCreado.arbolDeProblemas.id;
          console.log(this.arbolDeProblemas.descripcion);
          console.log("id arbol de problemas creado",this.arbolDeProblemas.id);



      // Crear causas en orden
      for (const c of this.causas.controls) {
        await this.crearCausaSecuencial(c);
      }

      // Crear efectos en orden
      for (const e of this.efectos.controls) {
        await this.crearEfectoSecuencial(e);
      }

    swal.fire('Nuevo Árbol de Problemas',`Árbol de Problemas: ${this.arbolDeProblemas.descripcion} creado con éxito!`, 'success');
    this.router.navigate(['/investigador/proyectosinvestigador/arboldeproblemasinvest',this.proyecto.id]);

    }catch(error){
        console.error('Error al crear el Árbol de Problemas:', error);
      }
    
  }

  public async crearCausaSecuencial(causaControl: any): Promise<void>{

    try{ 
      console.log('Causa creada:', causaControl.value.descripcion)
      this.causa.descripcion = causaControl.value.descripcion;
      this.causa.arbolDeProblemasId = this.arbolDeProblemas.id;
      console.log("id arbol de problemas",this.causa.arbolDeProblemasId);

      const causaCreada: any = await firstValueFrom(this.causaService.createCausa(this.causa));
      console.log('Causa creada en orden:');
      console.log(causaCreada.causa.descripcion);
      console.log("id causa",causaCreada.causa.id);

      //this.router.navigate(['/investigador/proyectosinvestigador/proyectosinvest']);

    }catch (err) {
      console.error('Error al crear causa:', err); 
    }
 
  }


  public async crearEfectoSecuencial(efectoControl): Promise<void>{

    try{ 
      console.log('Efecto creado:', efectoControl.value.descripcion)
      this.efecto.descripcion = efectoControl.value.descripcion;
      this.efecto.arbolDeProblemasId = this.arbolDeProblemas.id;
      console.log("id arbol de problemas",this.efecto.arbolDeProblemasId);

      const efectoCreado: any = await firstValueFrom(this.efectoService.createEfecto(this.efecto));
      console.log('Efecto creado en orden:');
      console.log(efectoCreado.efecto.descripcion);
      console.log("id efecto",efectoCreado.efecto.id);

      //this.router.navigate(['/investigador/proyectosinvestigador/proyectosinvest']);

    }catch(err){ 
        console.error('Error al crear efecto:', err);
    }

  }
 
  public async actualizarArbolDeProblemas():Promise<void>{
    
      swal.fire({
                 title: 'Está seguro?',
                 text: `¿Seguro que desea actualizar este árbol de problemas de problemas: ${this.arbolDeProblemas.descripcion}?`,
                 icon: 'warning',
                 showCancelButton: true,
                 confirmButtonColor: '#3085d6',
                 cancelButtonColor: '#007bff',
                 confirmButtonText: 'Si, actualizar!',
                 cancelButtonText: 'No, cancelar!',
                 customClass: {
                    confirmButton: 'btn btn-success',
                    cancelButton: 'btn btn-danger',
                  },
                  buttonsStyling: false,
                  reverseButtons: true
                }as any).then(async (result) => {
                if (result.value) {

                  try{

                      const formulario = this.formularioArbol.value;

                      // Editar los valores del formulario al objeto
                    
                      this.arbolDeProblemas.descripcion = formulario.descripcion;

                      // Editar el árbol de problemas primero
                      const arbolEditado: any = await firstValueFrom(
                        this.arbolDeProblemasService.updateArbolDeProblemas(this.arbolDeProblemas)
                      );
                      
                          //this.arbolDeProblemas.id = arbolEditado.id;
                          this.arbolDeProblemas.id = arbolEditado.arbolDeProblemas.id;
                          console.log(this.arbolDeProblemas.descripcion);
                          console.log("id arbol de problemas editado",this.arbolDeProblemas.id);

                      // Editar causas en orden
                      for (const c of this.causas.controls) {
                        await this.editarCausaSecuencial(c);
                      }

                      // Editar efectos en orden
                      for (const e of this.efectos.controls) {
                        await this.editarEfectoSecuencial(e);
                      }

                      console.log('árbol de problemas actualizado');
                      swal.fire('Árbol de Problemas Actualizado',`Árbol de Problemas: ${this.arbolDeProblemas.descripcion} actualizado con éxito!`, 'success');
                      this.router.navigate(['/investigador/proyectosinvestigador/arboldeproblemasinvest',this.arbolDeProblemas.proyectoId]);

                  }catch(error){
                      console.error('Error al crear el árbol de Problemas:', error);
                  }
               }else
                {
                  swal.fire(
                    'Actualización cancelada!',
                    `Actualización del árbol de problemas: ${this.arbolDeProblemas.descripcion} cancelada`,
                    'info'
                    )
                }
              })
      }


  public async editarCausaSecuencial(causaControl: any): Promise<void>{

    try{ 
      if(causaControl.dirty){ // solo si hubo cambios
        if(causaControl.value.id){ 
          console.log('id Causa editada hola:', causaControl.value.id)
          console.log('Causa editada:', causaControl.value.descripcion)
        
          this.causa.id = causaControl.value.id; // ahora sí se asigna el id
          this.causa.descripcion = causaControl.value.descripcion;
          this.causa.arbolDeProblemasId = this.arbolDeProblemas.id;

          const causaActualizada: any = await firstValueFrom(this.causaService.updateCausa(this.causa));
          console.log('Causa editada en orden:');
          console.log(causaActualizada.causa.descripcion);
          console.log("id causa",causaActualizada.causa.id);

          //this.router.navigate(['/investigador/proyectosinvestigador/proyectosinvest']);
        }else
        {
          try{ 
            console.log('Causa creada:', causaControl.value.descripcion)
            this.causa.descripcion = causaControl.value.descripcion;
            this.causa.arbolDeProblemasId = this.arbolDeProblemas.id;
            console.log("id arbol de problemas",this.causa.arbolDeProblemasId);

            const causaCreada : any = await firstValueFrom(this.causaService.createCausa(this.causa));
            console.log('Causa creada en orden:');
            console.log(causaCreada.causa.descripcion);
            console.log("id causa",causaCreada.causa.id);

            //this.router.navigate(['/investigador/proyectosinvestigador/proyectosinvest']);

          }catch (err) {
            console.error('Error al crear causa:', err); 
          }

        }
      }

    }catch (err) {
      console.error('Error al editar causa:', err); 
    }
 
  }


  public async editarEfectoSecuencial(efectoControl): Promise<void>{

    try{ 
      if(efectoControl.dirty){ 
        if(efectoControl.value.id){
          console.log('Efecto editado:', efectoControl.value.descripcion)
          this.efecto.id = efectoControl.value.id;
          this.efecto.descripcion = efectoControl.value.descripcion;
          this.efecto.arbolDeProblemasId = this.arbolDeProblemas.id;
          console.log("id arbol de problemas",this.efecto.arbolDeProblemasId);

          const efectoActualizado: any = await firstValueFrom(this.efectoService.updateEfecto(this.efecto));
          console.log('Efecto editado en orden:');
          console.log(efectoActualizado.efecto.descripcion);
          console.log("id efecto",efectoActualizado.efecto.id);

          //this.router.navigate(['/investigador/proyectosinvestigador/proyectosinvest']);
       }
       else
       {
          try{ 
            console.log('Efecto creado:', efectoControl.value.descripcion)
            this.efecto.descripcion = efectoControl.value.descripcion;
            this.efecto.arbolDeProblemasId = this.arbolDeProblemas.id;
            console.log("id arbol de problemas",this.efecto.arbolDeProblemasId);

            const efectoCreado: any = await firstValueFrom(this.efectoService.createEfecto(this.efecto));
            console.log('Efecto creado en orden:');
            console.log(efectoCreado.efecto.descripcion);
            console.log("id efecto",efectoCreado.efecto.id);

            //this.router.navigate(['/investigador/proyectosinvestigador/proyectosinvest']);

          }catch(err){ 
              console.error('Error al crear efecto:', err);
          }
        }
      }

    }catch(err){ 
        console.error('Error al editar efecto:', err);
    }

  }

  eliminarCausa(idCausa: number): void {

    swal.fire({
                title: 'Está seguro?',
                text: `¿Seguro que desea eliminar la causa: ${idCausa}?`,
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Si, eliminar!',
                cancelButtonText: 'No, cancelar!',
                customClass: {
                  confirmButton: 'btn btn-primary',
                  cancelButton: 'btn btn-danger',
            
                  },
                buttonsStyling: false,
                  reverseButtons: true
                } as any).then((result) => {
                  if (result.value) {
                    this.causaService.deleteCausa(idCausa).subscribe(
                     {
                      next:(data)  => { 
                      swal.fire(
                        'Causa  Eliminada!',
                        `Causa eliminada con éxito.`,
                        'success'
                      )
                       this.cargarCausas(); 

                      },
                      error:(err)  => { 
                        console.error('Error al eliminar causa:', err);
                       }
                    }
                  )
                  
                  }else
                  {
                    swal.fire(
                          'Eliminación cancelada!',
                          `Eliminación de causa cancelada`,
                          'info'
                    )
                  }
                })
}

  eliminarEfecto(idEfecto: number): void {

    swal.fire({
                title: 'Está seguro?',
                text: `¿Seguro que desea eliminar el efecto: ${idEfecto}?`,
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Si, eliminar!',
                cancelButtonText: 'No, cancelar!',
                customClass: {
                  confirmButton: 'btn btn-primary',
                  cancelButton: 'btn btn-danger',
            
                  },
                buttonsStyling: false,
                  reverseButtons: true
                } as any).then((result) => {
                  if (result.value) {
                    this.efectoService.deleteEfecto(idEfecto).subscribe(
                    {
                        next:(data) => { 

                        swal.fire(
                          'Efecto  Eliminado!',
                          `Efecto eliminado con éxito.`,
                          'success'
                        )
                        this.cargarEfectos(); 
                      },
                      error:(err) => { 
                          console.error('Error al eliminar efecto:', err);
                       }
                    }
                  )
                  
                  }else
                  {
                    swal.fire(
                          'Eliminación cancelada!',
                          `Eliminación de efecto cancelado`,
                          'info'
                    )
                  }
                })
}
    
 

  public cancelarArbolDeProblemas():void
  {
    const formulario = this.formularioArbol.value;

  
      if(!this.arbolDeProblemas.id)
      {
        swal.fire(
                'Creación cancelada!',
                `Creación del Árbol De Problemas: ${formulario.descripcion} cancelada`,
                'info'
                )
        this.router.navigate(['/investigador/proyectosinvestigador/arboldeproblemasinvest',this.proyecto.id]);
      }
      else
      {
        swal.fire(
                'Actualización cancelada!',
                `Actualización del Árbol De Problemas: ${formulario.descripcion} cancelada`,
                'info'
                )
        this.router.navigate(['/investigador/proyectosinvestigador/arboldeproblemasinvest',this.arbolDeProblemas.proyectoId]);        
      }
      
  }


  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {

}


}
