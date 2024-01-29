import {Component} from '@angular/core';

@Component({
  selector: 'app-admin-panel',
  templateUrl: './admin-panel.component.html',
  styleUrls: ['./admin-panel.component.css']
})
export class AdminPanelComponent {
  isLoggedIn: boolean = false;
  username: string = '';
  password: string = '';

  login() {
    if (this.username === 'admin' && this.password === 'admin') {
      this.isLoggedIn = true;
    } else {
      alert('Invalid username or password');
    }
  }
}
