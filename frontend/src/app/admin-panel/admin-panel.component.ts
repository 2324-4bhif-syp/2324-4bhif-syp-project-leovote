import {Component} from '@angular/core';
import {AdminService} from "../shared/control/admin.service";

@Component({
  selector: 'app-admin-panel',
  templateUrl: './admin-panel.component.html',
  styleUrls: ['./admin-panel.component.css']
})
export class AdminPanelComponent {
  username: string = '';
  password: string = '';

  login() {
    if (this.username === 'admin' && this.password === 'admin') {
      this.adminService.isLoggedIn = true;
    } else {
      alert('Invalid username or password');
    }
  }

  constructor(protected adminService: AdminService) { }
}
