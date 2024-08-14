import { Injectable, NgZone } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environment';

@Injectable({
    providedIn: 'root'
})
export class SseService {
    constructor(private zone: NgZone) { }

    getTicketUpdates(): Observable<any> {
        return new Observable(observer => {
            const eventSource = new EventSource(`${environment.apiUrl}/sse/updates`, { withCredentials: true });
            eventSource.onmessage = event => {
                this.zone.run(() => {
                    observer.next(JSON.parse(event.data));
                });
            };
            eventSource.onerror = error => {
                this.zone.run(() => {
                    observer.error(error);
                });
            };
            return () => {
                eventSource.close();
            };
        });
    }
}
