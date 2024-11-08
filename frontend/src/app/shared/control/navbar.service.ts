import { Injectable } from '@angular/core';
import {Subject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class NavbarService {
  private toggleSubject = new Subject<void>();


  constructor() {
  }

  toggle$ = this.toggleSubject.asObservable();

  toggleSidenav() {
    this.toggleSubject.next();
  }
}
