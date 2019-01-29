# TP2 :  Fragment, NavigationDrawer, AsyncTask

## Instructions 

- faire des commits réguliers et commentés à bon escient
- faire un push en fin de séance

## Fragments

Pour plus de détails lire la documentation officielle : https://developer.android.com/guide/components/fragments.html

Un fragment est un composant réutilisable qui est rattaché à une activité. Une activité peut contenir de multiples fragments. Un fragment peut contenir une vue  ; pour être visible, le fragment doit être inséré dans le layout de l'activité courante. Le fragment possède un cycle de vie qui lui est propre, mais qui est déterminé par le cycle de vie de l'activité hôte.
Un fragment peut être créé et inséré dynamiquement au sein d'une vue par le biais d'une transaction gérée par le manager de fragment `FragmentManager`.
Si un fragment possède une vue, sa description peut être définie  dans un fichier XML.

### Exercice

- Créer un nouveau projet vide.		
- Importer le package : `import android.support.v4.app.Fragment;`
- Définir deux fragments (c'est-à-dire deux classes dérivant de `Fragment`). Redéfinir les méthodes `onCreate()` et `onCreateView`.
- La méthode `onCreateView` devrait ressembler à ça :
	
```java
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	super.onCreateView(inflater, container, savedInstanceState);
	return inflater.inflate(R.layout.fragment,container,false);
}
```				
- Dans cet exemple, la vue du fragment est décrite dans le fichier XML `res/layout/fragment.xml`, "intégrée" (inflated) dans le fragment puis retournée par la méthode.
- Créer un layout XML pour l'activité (le layout principal) qui comporte deux boutons et un `LinearLayout` qui jouera le rôle de "container" pour afficher un fragment.
- Créer un layout XML pour chaque fragment qui contient un texte, un bouton et une image.
- Quand le bouton 1 est cliqué, le fragment 1 est créé et ajouté dynamiquement au layout principal, dans le `LinearLayout` prévu à cet effet.
- De la même manière, quand le bouton 2 est cliqué, le fragment 2 est créé puis affiché

La gestion dynamique des fragments passe par le manager de fragments. On récupère le manager en appelant dans l'activité la méthode `getSupportFragmentManager()` puis on effectue une transaction à partir du manager avec la classe `FragmentTransaction`.	On peut ajouter une animation en utilisant la méthode `setTransition()`. À la fin de la transaction, on valide la transaction par un `commit`.				

Pour ajouter un nouveau fragment au layout principal, on écrit donc :

```java
fragment = new MyFragment(); // MyFragment est une sous-classe de Fragment
FragmentManager manager = getSupportFragmentManager();
FragmentTransaction transaction = manager.beginTransaction();
transaction.replace(R.id.myLayout, fragment);
transaction.commit();
```

## NavigationDrawer

https://developer.android.com/training/implementing-navigation/nav-drawer.html

C'est un panneau rétractable qui affiche des options de navigation au sein d'une application. Généralement le `NavigationDrawer` s'utilise avec des fragments : chaque sélection d'une option affiche un nouveau fragment dans la vue principale.

### Exercice
1. En utilisant le template `NavigationDrawer Activity` d'Android Studio et la documentation, reprendre l'application précédente pour passer d'un fragment à l'autre en utilisant le `NavigationDrawer`.
2. Étendre l'application de manière à faire communiquer les deux fragments :
	- le fragment 1 possède un `TextView` qui affiche le contenu d'un `EditText` du fragment 2 
	- et idem pour le fragment 2
