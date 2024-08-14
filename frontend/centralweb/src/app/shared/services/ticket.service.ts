import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Ticket, TicketsResponse } from '../models/ticket.model';
import { environment } from '../../../../environment';

@Injectable({
  providedIn: 'root'
})
export class TicketService {
  private apiUrl = `${environment.apiUrl}/tickets`;

  constructor(private http: HttpClient) { }

  getTicketsByUserIdAndPage(userId: number, filter?: string, page: number = 0, size: number = 10): Observable<TicketsResponse> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', "id");;

    if (filter) {
      params = params.append('filter', filter);
    }
    return this.http.get<TicketsResponse>(`${this.apiUrl + "/user/"}${userId}`, { params, withCredentials: true });
  }

  createTicket(ticketDTO: { userId: number; subject: string; description: string; }): Observable<Ticket> {
    return this.http.post<Ticket>(`${this.apiUrl}`, ticketDTO, { withCredentials: true });
  }

  closeTicket(ticketId: number): Observable<any> {
    return this.http.patch(`${this.apiUrl}/${ticketId}/close`, null, { withCredentials: true });
  }

}
