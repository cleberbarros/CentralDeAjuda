import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { FeedbackService } from './feedback.service';
import { CreateTicketFeedback, TicketFeedback } from '../models/feedback.model';
import { environment } from '../../../../environment';

describe('FeedbackService', () => {
  let service: FeedbackService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [FeedbackService]
    });

    service = TestBed.inject(FeedbackService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('submitFeedback should post feedback data', () => {
    const mockFeedbackData: CreateTicketFeedback = {
      ticketId: 1,
      userId: 1,
      rating: 5,
      comment: 'Great service!'
    };

    const mockResponse: TicketFeedback = {
      id: 100,
      ...mockFeedbackData,
      createdAt: new Date()
    };

    service.submitFeedback(mockFeedbackData).subscribe(response => {
      expect(response).toEqual(mockResponse);
    });

    const req = httpTestingController.expectOne(`${environment.apiUrl}/feedback`);
    expect(req.request.method).toEqual('POST');
    expect(req.request.body).toEqual(mockFeedbackData);
    req.flush(mockResponse);
  });

  it('getFeedback should retrieve specific feedback', () => {
    const ticketId = 1;
    const mockResponse: TicketFeedback = {
      id: 100,
      ticketId: ticketId,
      userId: 1,
      rating: 5,
      comment: 'Very responsive',
      createdAt: new Date()
    };

    service.getFeedback(ticketId).subscribe(response => {
      expect(response).toEqual(mockResponse);
    });

    const req = httpTestingController.expectOne(`${environment.apiUrl}/feedback/${ticketId}`);
    expect(req.request.method).toEqual('GET');
    req.flush(mockResponse);
  });

});
