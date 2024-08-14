import { TestBed, ComponentFixture } from '@angular/core/testing';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { TicketFeedbackComponent } from './ticket-feedback.component';
import { FeedbackService } from '../../shared/services/feedback.service';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of, throwError } from 'rxjs';
import { CreateTicketFeedback } from '../../shared/models/feedback.model';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';

describe('TicketFeedbackComponent', () => {
  let component: TicketFeedbackComponent;
  let fixture: ComponentFixture<TicketFeedbackComponent>;
  let mockSnackBar: jasmine.SpyObj<MatSnackBar>;
  let mockDialogRef: jasmine.SpyObj<MatDialogRef<TicketFeedbackComponent>>;
  let mockFeedbackService: jasmine.SpyObj<FeedbackService>;

  beforeEach(async () => {
    mockSnackBar = jasmine.createSpyObj('MatSnackBar', ['open']);
    mockDialogRef = jasmine.createSpyObj('MatDialogRef', ['close']);
    mockFeedbackService = jasmine.createSpyObj('FeedbackService', ['submitFeedback']);

    await TestBed.configureTestingModule({
      imports: [
        FormsModule,
        NoopAnimationsModule,
        MatSnackBarModule,
        HttpClientTestingModule,
        MatFormFieldModule,
        MatInputModule,
        MatButtonModule,
        MatIconModule,
        ReactiveFormsModule,
        TicketFeedbackComponent  
      ],
      providers: [
        { provide: FeedbackService, useValue: mockFeedbackService },
        { provide: MatSnackBar, useValue: mockSnackBar },
        { provide: MatDialogRef, useValue: mockDialogRef },
        { provide: MAT_DIALOG_DATA, useValue: { ticketId: 123, userId: 1 } }
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(TicketFeedbackComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
});


  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set rating on rate', () => {
    const star = 5;
    component.rate(star);
    expect(component.rating).toBe(star);
  });

  it('should submit feedback and close dialog on success', () => {
    const feedbackData: CreateTicketFeedback = {
      ticketId: 123,
      userId: 1,
      rating: 5,
      comment: 'Great service!'
    };
    mockFeedbackService.submitFeedback.and.returnValue(of({
      id: 1,
      ticketId: 123,
      userId: 1,
      rating: 5,
      comment: 'Great service!',
      createdAt: new Date()
    }));

    component.rating = feedbackData.rating;
    component.comment = feedbackData.comment;

    component.submitFeedback();

    fixture.detectChanges();
    expect(mockFeedbackService.submitFeedback).toHaveBeenCalledWith(feedbackData);
    expect(mockSnackBar.open).toHaveBeenCalledWith('Feedback submitted successfully', 'Close', { duration: 3000 });
    expect(mockDialogRef.close).toHaveBeenCalled();
  });

  it('should display error message on feedback submission failure', () => {
    mockFeedbackService.submitFeedback.and.returnValue(throwError(new Error('Test Error')));
    component.submitFeedback();

    fixture.detectChanges();
    expect(mockSnackBar.open).toHaveBeenCalledWith('Error submitting feedback', 'Close', { duration: 3000 });
  });
});
