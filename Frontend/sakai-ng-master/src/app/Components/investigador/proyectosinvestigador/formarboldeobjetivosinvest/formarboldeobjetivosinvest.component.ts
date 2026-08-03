import { Component, OnDestroy, OnInit } from '@angular/core';
import { Proyecto } from '@app/Models/proyecto.model';
import { ProyectoService } from '@app/Services/proyecto.service';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '@app/Services/auth.service';
import { User } from '@app/Models/user.model'; 
import { ArbolDeProblemas } from '@app/Models/arboldeproblemas.model';
import { ArbolDeProblemasService } from '@app/Services/arboldeproblemas.service';
import { ArbolDeObjetivos } from '@app/Models/arboldeobjetivos.model';
import { ArbolDeObjetivosService } from '@app/Services/arboldeobjetivos.service';
import { Causa } from '@app/Models/causa.model';
import { CausaService } from '@app/Services/causa.service';
import { Efecto } from '@app/Models/efecto.model';
import { EfectoService } from '@app/Services/efecto.service';
import { Medio } from '@app/Models/medio.model';
import { MedioService } from '@app/Services/medio.service';
import { Fin } from '@app/Models/fin.model';
import { FinService } from '@app/Services/fin.service';
import { FormBuilder, FormGroup, FormArray } from '@angular/forms';
import { firstValueFrom } from 'rxjs';
import { Validators } from '@angular/forms';
import swal from 'sweetalert2';


@Component({
  selector: 'app-formarboldeobjetivosinvest',
  templateUrl: './formarboldeobjetivosinvest.component.html',
  styleUrls: ['./formarboldeobjetivosinvest.component.css']


})

export class FormArbolDeObjetivosInvestComponent implements OnInit,OnDestroy{

  currentUser: User | null = null;

  public proyecto: Proyecto = new Proyecto();

  public tituloArbolDeObjetivos: string = 'Árbol de Objetivos';

  public tituloMediosArbolDeObjetivos: string = 'Medios del Árbol de Objetivos';

  public tituloFinesArbolDeObjetivos: string = 'Fines del Árbol de Objetivos';

  public arbolDeProblemas: ArbolDeProblemas = new ArbolDeProblemas();
  
  public arbolDeObjetivos: ArbolDeObjetivos = new ArbolDeObjetivos();
  
  public causa: Causa = new Causa();

  public causasArbolDeProblemas: Causa[] = [];

  public efecto: Efecto = new Efecto();

  public efectosArbolDeProblemas: Efecto[] = [];

  public medio: Medio = new Medio();

  public mediosArbolDeObjetivos: Medio[] = [];

  public fin: Fin = new Fin();

  public finesArbolDeObjetivos: Fin[] = [];

  formularioCausas: FormGroup;

  formularioEfectos: FormGroup;

  formularioArbol: FormGroup;

  constructor(private proyectoService: ProyectoService,
              private arbolDeProblemasService: ArbolDeProblemasService,
              private arbolDeObjetivosService: ArbolDeObjetivosService,
              private causaService: CausaService,
              private efectoService: EfectoService,
              private medioService: MedioService,
              private finService: FinService,
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
                    medios: this.fb.array([]),
                    fines: this.fb.array([])
                  },{
                      validators: [this.validarCantidadCausasEfectos]  // validación personalizada
                    });  
                                }

  ngOnInit(): void {

   this.authService.currentUser.subscribe(user => this.currentUser = user);

   this.cargarProyecto();
   this.cargarArbolDeObjetivos(); 

  }

  /*get causas(): FormArray {
    return this.formularioCausas.get('causas') as FormArray;
  }*/

  get causas(): FormArray {
    return this.formularioArbol.get('causas') as FormArray;
  }

  agregarCausas(): void {
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
  
  get medios(): FormArray {
    return this.formularioArbol.get('medios') as FormArray;
  }

  agregarMedios(): void {
    this.medios.push(this.fb.group({
      id: [null], // se quedará null hasta que el backend devuelva uno
      descripcion: ['', [Validators.required, Validators.minLength(5)]], // Inicializa el campo con un string vacío
      esExistente: [false] // nuevo input
    }));
  }

  private crearMedioFormGroup(medio: Medio): FormGroup {
  return this.fb.group({
    id: [medio.id],
    descripcion: [medio.descripcion || '', [Validators.required, Validators.minLength(5)]],
    esExistente: [!!medio.id] // true si ya viene de backend
  });
}


  /*get efectos(): FormArray {
    return this.formularioEfectos.get('efectos') as FormArray;
  }*/

  get efectos(): FormArray {
    return this.formularioArbol.get('efectos') as FormArray;
  }

  agregarEfectos(): void {
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
  
  get fines(): FormArray {
    return this.formularioArbol.get('fines') as FormArray;
  }

  agregarFines(): void {
    this.fines.push(this.fb.group({
      id: [null], // se quedará null hasta que el backend devuelva uno
      descripcion: ['', [Validators.required, Validators.minLength(5)]],  // Inicializa el campo con un string vacío
      esExistente: [false] // nuevo input
    }));
  }

  private crearFinFormGroup(fin: Fin): FormGroup {
  return this.fb.group({
    id: [fin.id],
    descripcion: [fin.descripcion || '', [Validators.required, Validators.minLength(5)]],
    esExistente: [!!fin.id] // true si ya viene de backend
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

    const mediosValidas = this.medios.length > 0 && this.medios.controls.every(c => c.valid);
    const finesValidos = this.fines.length > 0 && this.fines.controls.every(e => e.valid);

    const cantidadesIguales = !this.formularioArbol.hasError('cantidadNoCoincide');

    return !!(descripcionValida && mediosValidas && finesValidos && cantidadesIguales);
 }

  cargarProyecto(): void{
    this.activatedRoute.params.subscribe(params => {
      let id = params['idproyecto']
      if(id){
        this.proyectoService.getProyecto(id).subscribe( (proyecto) =>
          {this.proyecto = proyecto;
            console.log('id del proyecto ok:', this.proyecto);
            this.cargarArbolDeProblemas();
          })

      }
    })
  }

   cargarArbolDeProblemas(): void {
      console.log("id proyecto en cargar arbol de problemas:", this.proyecto.id);
      this.arbolDeProblemasService.getArbolDeProblemasPorIdProyecto(this.proyecto.id).subscribe({
        next: (arbolDeProblemas) => {
          if (arbolDeProblemas) {
            this.arbolDeProblemas = arbolDeProblemas;
            this.formularioArbol.patchValue({
              descripcion: this.arbolDeProblemas.descripcion
            });
            
            this.cargarCausas();
            this.cargarEfectos();
            console.log("arbol de problemas:", this.arbolDeProblemas);
          } else {
            console.log("El árbol de problemas no existe para este proyecto.");
            this.arbolDeProblemas = null;
          }
        },
        error: (e) => {
            console.error('Error al cargar árbol de problemas:', e);
        }
      });
  }

  cargarArbolDeObjetivos(): void{
    this.activatedRoute.params.subscribe(params => {
      let id = params['idarbol']
      if(id){
        this.arbolDeObjetivosService.getArbolDeObjetivos(id).subscribe(
          {
            next: (arbolDeObjetivos) => { 
            
            this.arbolDeObjetivos = arbolDeObjetivos;
            console.log('descripcion del arbol de objetivos ok:', this.arbolDeObjetivos.descripcion);
            // Actualizar valores en el formulario
            this.formularioArbol.patchValue({
              descripcion: this.arbolDeObjetivos.descripcion
            });
            this.cargarMedios();
            this.cargarFines();
            console.log('id del arbol de objetivos ok:', this.arbolDeObjetivos);
          
            },
            error: (e) =>{ 
                console.error('Error al cargar árbol de objetivos:', e);
              }

          });
      }else
      {
        console.log("El árbol de objetivos no existe para este proyecto.");
      }
    })
  }
   

  cargarCausas():void
    {

      console.log('dentro de cargar causas:');
      this.causaService.getCausasPorIdArbolDeProblemas(this.arbolDeProblemas.id).subscribe({
        next: (causas) => {
              this.causasArbolDeProblemas =  Array.isArray(causas) ? causas : [];
              //const causasArray = this.formularioArbol.get('causas') as FormArray;
              //causasArray.clear(); // Limpia el FormArray antes de llenarlo

              /*this.causasArbolDeProblemas.forEach(causa => {
                causasArray.push(this.crearCausaFormGroup(causa));
              });
              */
            const mediosArray = this.formularioArbol.get('medios') as FormArray;
            mediosArray.clear();
            this.causasArbolDeProblemas.forEach(causa => {
              
              mediosArray.push(this.crearMedioFormGroup({
                id: 0, descripcion: causa.descripcion,
                semaforo: '',
                arbolDeObjetivosId: 0
              }));
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
              /*const efectosArray = this.formularioArbol.get('efectos') as FormArray;
              efectosArray.clear(); // Limpia el FormArray antes de llenarlo

              this.efectosArbolDeProblemas.forEach(efecto => {
                efectosArray.push(this.crearEfectoFormGroup(efecto));
              });*/

            const finesArray = this.formularioArbol.get('fines') as FormArray;
            finesArray.clear();
            this.efectosArbolDeProblemas.forEach(efecto => {
              
              finesArray.push(this.crearFinFormGroup({
                id: 0, descripcion: efecto.descripcion,
                semaforo: '',
                arbolDeObjetivosId: 0
              }));
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

   cargarMedios():void
      {
        this.medioService.getMediosPorIdArbolDeObjetivos(this.arbolDeObjetivos.id).subscribe({
          next: (medios) => {
                this.mediosArbolDeObjetivos =  Array.isArray(medios) ? medios : [];
                const mediosArray = this.formularioArbol.get('medios') as FormArray;
                mediosArray.clear(); // Limpia el FormArray antes de llenarlo
  
                this.mediosArbolDeObjetivos.forEach(medio => {
                  mediosArray.push(this.crearMedioFormGroup(medio));
                });
    
              if(this.mediosArbolDeObjetivos.length == 0)
                 //swal.fire('lista vacia', `${respose.mensaje}:`, 'success')
                 console.log('lista vacia');
      
          },
          error: (e) => {
            console.error('Error al cargar medios:', e);
    
          }
        });
    
      }  

  cargarFines():void
      {
        this.finService.getFinesPorIdArbolDeObjetivos(this.arbolDeObjetivos.id).subscribe({
          next: (fines) => {
                this.finesArbolDeObjetivos =  Array.isArray(fines) ? fines : [];
                const finesArray = this.formularioArbol.get('fines') as FormArray;
                finesArray.clear(); // Limpia el FormArray antes de llenarlo
  
                this.finesArbolDeObjetivos.forEach(fin => {
                  finesArray.push(this.crearFinFormGroup(fin));
                });
    
              if(this.finesArbolDeObjetivos.length == 0)
                 //swal.fire('lista vacia', `${respose.mensaje}:`, 'success')
                 console.log('lista vacia');
    
    
          },
          error: (e) => {
            console.error('Error al cargar fines:', e);
    
          }
        });
    
      }  

   public async crearArbolDeObjetivos():  Promise<void> {

    try{

      const formulario = this.formularioArbol.value;

      // Asignar los valores del formulario al objeto
      this.arbolDeObjetivos.descripcion = formulario.descripcion;
      this.arbolDeObjetivos.proyectoId = this.proyecto.id;

      // Crear el árbol de objetivos primero
      const arbolCreado: any = await firstValueFrom(
        this.arbolDeObjetivosService.createArbolDeObjetivos(this.arbolDeObjetivos)
      );
      
          this.arbolDeObjetivos.id = arbolCreado.arbolDeObjetivos.id;
          console.log(this.arbolDeObjetivos.descripcion);
          console.log("id arbol de objetivos creado",this.arbolDeObjetivos.id);

      // Crear medios en orden
      for (const c of this.medios.controls) {
        await this.crearMedioSecuencial(c);
      }

      // Crear fines en orden
      for (const e of this.fines.controls) {
        await this.crearFinSecuencial(e);
      }

    swal.fire('Nuevo Árbol de Objetivos',`Árbol de Objetivos: ${this.arbolDeObjetivos.descripcion} creado con éxito!`, 'success');
    this.router.navigate(['/investigador/proyectosinvestigador/arboldeobjetivosinvest',this.proyecto.id]);

    }catch(error){
        console.error('Error al crear el Árbol de Objetivos:', error);
      }
    
  }

  public async crearMedioSecuencial(medioControl: any): Promise<void>{

    try{ 
      console.log('Medio creado:',medioControl.value.descripcion)
      this.medio.descripcion = medioControl.value.descripcion;
      this.medio.arbolDeObjetivosId = this.arbolDeObjetivos.id;
      console.log("id arbol de objetivos",this.medio.arbolDeObjetivosId);

      const medioCreado: any = await firstValueFrom(this.medioService.createMedio(this.medio));
      console.log('Medio creado en orden:');
      console.log(medioCreado.medio.descripcion);
      console.log("id medio",medioCreado.medio.id);

      //this.router.navigate(['/investigador/proyectosinvestigador/proyectosinvest']);

    }catch (err) {
      console.error('Error al crear medio:', err); 
    }
 
  }


  public async crearFinSecuencial(finControl): Promise<void>{

    try{ 
      console.log('Fin creado:', finControl.value.descripcion)
      this.fin.descripcion = finControl.value.descripcion;
      this.fin.arbolDeObjetivosId = this.arbolDeObjetivos.id;
      console.log("id arbol de objetivos",this.fin.arbolDeObjetivosId);

      const finCreado: any = await firstValueFrom(this.finService.createFin(this.fin));
      console.log('Fin creado en orden:');
      console.log(finCreado.fin.descripcion);
      console.log("id fin",finCreado.fin.id);

      //this.router.navigate(['/investigador/proyectosinvestigador/proyectosinvest']);

    }catch(err){ 
        console.error('Error al crear fin:', err);
    }

  }
 
  public async actualizarArbolDeObjetivos():Promise<void>{
    
      swal.fire({
                 title: 'Está seguro?',
                 text: `¿Seguro que desea actualizar este árbol de objetivos: ${this.arbolDeObjetivos.descripcion}?`,
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
                      this.arbolDeObjetivos.descripcion = formulario.descripcion;

                      // Editar el árbol de objetivos primero
                      const arbolEditado: any = await firstValueFrom(
                        this.arbolDeObjetivosService.updateArbolDeObjetivos(this.arbolDeObjetivos)
                      );
                      
                          this.arbolDeObjetivos.id = arbolEditado.arbolDeObjetivos.id;
                          console.log(this.arbolDeObjetivos.descripcion);
                          console.log("id arbol de objetivos editado",this.arbolDeObjetivos.id);

                      // Editar medios en orden
                      for (const c of this.medios.controls) {
                        await this.editarMedioSecuencial(c);
                      }

                      // Editar fines en orden
                      for (const e of this.fines.controls) {
                        await this.editarFinSecuencial(e);
                      }

                      console.log('árbol de objetivos actualizado');
                      swal.fire('Árbol de Objetivos Actualizado',`Árbol de Objetivos: ${this.arbolDeObjetivos.descripcion} actualizado con éxito!`, 'success');
                      this.router.navigate(['/investigador/proyectosinvestigador/arboldeobjetivosinvest',this.arbolDeObjetivos.proyectoId]);

                  }catch(error){
                      console.error('Error al crear el árbol de Objetivos:', error);
                  }
               }else
                {
                  swal.fire(
                    'Actualización cancelada!',
                    `Actualización del árbol de objetivos: ${this.arbolDeObjetivos.descripcion} cancelada`,
                    'info'
                    )
                }
              })
      }


  public async editarMedioSecuencial(medioControl: any): Promise<void>{

    try{ 
      if(medioControl.dirty){ // solo si hubo cambios
        if(medioControl.value.id){ 
          console.log('id Medio editada hola:', medioControl.value.id)
          console.log('Medio editada:', medioControl.value.descripcion)
        
          this.medio.id = medioControl.value.id; // ahora sí se asigna el id
          this.medio.descripcion = medioControl.value.descripcion;
          this.medio.arbolDeObjetivosId = this.arbolDeObjetivos.id;

          const medioActualizado: any = await firstValueFrom(this.medioService.updateMedio(this.medio));
          console.log('Medio editado en orden:');
          console.log(medioActualizado.medio.descripcion);
          console.log("id medio",medioActualizado.medio.id);

          //this.router.navigate(['/investigador/proyectosinvestigador/proyectosinvest']);
        }else
        {
          try{ 
            console.log('Medio creado:', medioControl.value.descripcion)
            this.medio.descripcion = medioControl.value.descripcion;
            this.medio.arbolDeObjetivosId = this.arbolDeObjetivos.id;
            console.log("id arbol de objetivos",this.medio.arbolDeObjetivosId);

            const medioCreado: any = await firstValueFrom(this.medioService.createMedio(this.medio));
            console.log('Medio creado en orden:');
            console.log(medioCreado.medio.descripcion);
            console.log("id medio",medioCreado.medio.id);

            //this.router.navigate(['/investigador/proyectosinvestigador/proyectosinvest']);

          }catch (err) {
            console.error('Error al crear medio:', err); 
          }

        }
      }

    }catch (err) {
      console.error('Error al editar medio:', err); 
    }
 
  }


  public async editarFinSecuencial(finControl): Promise<void>{

    try{ 
      if(finControl.dirty){ 
        if(finControl.value.id){
          console.log('Fin editado:', finControl.value.descripcion)
          this.fin.id = finControl.value.id;
          this.fin.descripcion = finControl.value.descripcion;
          this.fin.arbolDeObjetivosId = this.arbolDeObjetivos.id;
          console.log("id arbol de objetivos",this.fin.arbolDeObjetivosId);

          const finActualizado: any = await firstValueFrom(this.finService.updateFin(this.fin));
          console.log('Fin editado en orden:');
          console.log(finActualizado.fin.descripcion);
          console.log("id fin",finActualizado.fin.id);

          //this.router.navigate(['/investigador/proyectosinvestigador/proyectosinvest']);
       }
       else
       {
          try{ 
            console.log('Fin creado:', finControl.value.descripcion)
            this.fin.descripcion = finControl.value.descripcion;
            this.fin.arbolDeObjetivosId = this.arbolDeObjetivos.id;
            console.log("id arbol de objetivos",this.fin.arbolDeObjetivosId);

            const finCreado = await firstValueFrom(this.finService.createFin(this.fin));
            console.log('Fin creado en orden:');
            console.log(finCreado.fin.descripcion);
            console.log("id fin",finCreado.fin.id);

            //this.router.navigate(['/investigador/proyectosinvestigador/proyectosinvest']);

          }catch(err){ 
              console.error('Error al crear fin:', err);
          }
        }
      }

    }catch(err){ 
        console.error('Error al editar fin:', err);
    }

  }

 
  public cancelarArbolDeObjetivos():void
  {
    const formulario = this.formularioArbol.value;

  
      if(!this.arbolDeObjetivos.id)
      {
        swal.fire(
                'Creación cancelada!',
                `Creación del Árbol De Objetivos: ${formulario.descripcion} cancelado`,
                'info'
                )
        this.router.navigate(['/investigador/proyectosinvestigador/arboldeobjetivosinvest',this.proyecto.id]);
      }
      else
      {
        swal.fire(
                'Actualización cancelada!',
                `Actualización del Árbol De Objetivos: ${formulario.descripcion} cancelado`,
                'info'
                )
        this.router.navigate(['/investigador/proyectosinvestigador/arboldeobjetivosinvest',this.arbolDeObjetivos.proyectoId]);        
      }
      
  }


  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {

}


}

