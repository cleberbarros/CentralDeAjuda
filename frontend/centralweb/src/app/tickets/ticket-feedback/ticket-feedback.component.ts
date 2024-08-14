import { Component, Inject } from '@angular/core';
import { FeedbackService } from '../../shared/services/feedback.service';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CreateTicketFeedback } from '../../shared/models/feedback.model';
import { FormsModule } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { CommonModule } from '@angular/common';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-ticket-feedback',
  templateUrl: './ticket-feedback.component.html',
  styleUrls: ['./ticket-feedback.component.css'],
  standalone: true,
  imports: [MatIconModule, MatFormFieldModule, MatInputModule, MatButtonModule, FormsModule, CommonModule]
})
export class TicketFeedbackComponent {
  rating: number = 0;
  comment: string = '';

  constructor(private feedbackService: FeedbackService,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public dialogRef: MatDialogRef<TicketFeedbackComponent>,
    private snackBar: MatSnackBar) { }

  rate(star: number) {
    this.rating = star;
  }

  submitFeedback() {
    const feedbackData: CreateTicketFeedback = {
      ticketId: this.data.ticketId,
      userId: this.data.userId,
      rating: this.rating,
      comment: this.comment
    };

    this.feedbackService.submitFeedback(feedbackData).subscribe({
      next: (response) => {
        this.snackBar.open('Feedback submitted successfully', 'Close', { duration: 3000 });
        this.dialogRef.close(response);
      },
      error: (err) => {
        console.error('There was an error!', err);
        this.snackBar.open('Error submitting feedback', 'Close', { duration: 3000 });
      }
    });
  }

}
