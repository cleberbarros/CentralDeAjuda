import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CreateTicketFeedback, TicketFeedback } from '../models/feedback.model';
import { environment } from '../../../../environment';

@Injectable({
  providedIn: 'root'
})
export class FeedbackService {
  private apiUrl = `${environment.apiUrl}/feedback`;

  constructor(private http: HttpClient) { }

  submitFeedback(feedbackData: CreateTicketFeedback): Observable<TicketFeedback> {
    return this.http.post<TicketFeedback>(this.apiUrl, feedbackData, { withCredentials: true });
  }

  getFeedback(ticketId: number): Observable<TicketFeedback> {
    return this.http.get<TicketFeedback>(`${this.apiUrl}/${ticketId}`, { withCredentials: true });
  }

}
