import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MqMessage } from './mq-message/mq-message.component';
@Component({
  selector: 'app-root',
  imports: [RouterOutlet,MqMessage],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App  {
  protected readonly title = signal('testapp');

}
