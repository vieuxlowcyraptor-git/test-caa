import { Component, OnDestroy, OnInit } from '@angular/core';
import { RestService } from '../service/rest.service';
import { interval, Subscription, switchMap, timer } from 'rxjs';
import { Message } from '../model/dtos';
import { CommonModule } from '@angular/common';

@Component({
    selector: 'mq-messages',
	imports: [CommonModule],
    templateUrl: './mq-message.component.html',
    styleUrl: './mq-message.component.css',
})

export class MqMessage  implements OnInit, OnDestroy {
    private pollingSubscription?: Subscription;
    public mqMessages: Message[]=[];
    constructor(private readonly rest: RestService) {
    }
    ngOnInit(): void {
        this.startPolling();
    }


    startPolling() {
        this.pollingSubscription = timer(0, 5000)       // immediate then every 10 seconds
            .pipe(
                switchMap(() => this.rest.getMqMessage(5))   // Cancel previous request if new one starts
            )
            .subscribe({
                next: (data) => {
                    this.mqMessages = data;
                },
                error: (err) => console.error('Polling error:', err)
            });
    }

    ngOnDestroy() {
        this.pollingSubscription?.unsubscribe();   // Important: prevent memory leaks
    }
}
