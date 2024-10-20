import { Component } from '@angular/core';
import { CdkDragDrop, transferArrayItem, moveItemInArray } from '@angular/cdk/drag-drop';

@Component({
  selector: 'app-multivalue-vote',
  templateUrl: './multivalue-vote.component.html',
  styleUrls: ['./multivalue-vote.component.css'],
})
export class MultivalueVoteComponent {
  // Liste der Filme auf der linken Seite
  movies = [
    {
      title: 'Episode I - The Phantom Menace',
      poster: 'https://upload.wikimedia.org/wikipedia/en/4/40/Star_Wars_Phantom_Menace_poster.jpg',
    },
    {
      title: 'Episode II - Attack of the Clones',
      poster:
        'https://upload.wikimedia.org/wikipedia/en/3/32/Star_Wars_-_Episode_II_Attack_of_the_Clones_%28movie_poster%29.jpg',
    },
    {
      title: 'Episode III - Revenge of the Sith',
      poster:
        'https://upload.wikimedia.org/wikipedia/en/9/93/Star_Wars_Episode_III_Revenge_of_the_Sith_poster.jpg',
    },
    {
      title: 'Episode IV - A New Hope',
      poster: 'https://upload.wikimedia.org/wikipedia/en/8/87/StarWarsMoviePoster1977.jpg',
    },
    {
      title: 'Episode V - The Empire Strikes Back',
      poster:
        'https://upload.wikimedia.org/wikipedia/en/3/3f/The_Empire_Strikes_Back_%281980_film%29.jpg',
    },
    {
      title: 'Episode VI - Return of the Jedi',
      poster: 'https://upload.wikimedia.org/wikipedia/en/b/b2/ReturnOfTheJediPoster1983.jpg',
    },
    {
      title: 'Episode VII - The Force Awakens',
      poster:
        'https://upload.wikimedia.org/wikipedia/en/a/a2/Star_Wars_The_Force_Awakens_Theatrical_Poster.jpg',
    },
    {
      title: 'Episode VIII - The Last Jedi',
      poster: 'https://upload.wikimedia.org/wikipedia/en/7/7f/Star_Wars_The_Last_Jedi.jpg',
    },
    {
      title: 'Episode IX – The Rise of Skywalker',
      poster:
        'https://upload.wikimedia.org/wikipedia/en/a/af/Star_Wars_The_Rise_of_Skywalker_poster.jpg',
    },
  ];

  // Bewertungsboxen (leere Listen für 6, 5, 4, 3, 2, 1 Punkte)
  points = [6, 5, 4, 3, 2, 1];
  ratedMovies: { title: string; poster: string }[][] = [[], [], [], [], [], []];

  // IDs für DropListen zur Verbindung zwischen allen Listen
  ratingListIds = this.points.map((_, i) => `rating-list-${i}`);
  allDropLists = ['movies-list', ...this.ratingListIds];

  // Verschieben von Filmen in der linken Liste oder innerhalb der Punkteboxen
  drop(event: CdkDragDrop<{ title: string; poster: string }[]>) {
    if (event.previousContainer === event.container) {
      // Filme innerhalb der linken Liste verschieben
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    }
  }

  // Filme von der linken Liste in die Bewertungsboxen oder zurück verschieben
  dropToRating(event: CdkDragDrop<{ title: string; poster: string }[]>, index: number) {
    // Blockiere, wenn bereits ein Film in der Bewertungsbox ist
    if (event.previousContainer !== event.container && this.ratedMovies[index].length === 0) {
      // Film aus der linken Liste oder einer anderen Bewertungsbox in diese Bewertungsbox verschieben
      transferArrayItem(
        event.previousContainer.data,
        this.ratedMovies[index],
        event.previousIndex,
        event.currentIndex
      );
    } else if (event.previousContainer !== event.container && event.container.id === 'movies-list') {
      // Film von einer Bewertungsbox zurück in die ursprüngliche Liste verschieben
      transferArrayItem(
        event.previousContainer.data,
        this.movies,
        event.previousIndex,
        event.currentIndex
      );
    }
  }
}
