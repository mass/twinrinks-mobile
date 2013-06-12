#import "Game.h"
#import "Team.h"
#import "MemoryManager.h"

@interface VC_Upcoming : UITableViewController

@property (strong, nonatomic) MemoryManager *myManager;
@property (nonatomic,strong) __block NSArray *gameArray;
@property (nonatomic,strong) __block NSArray *teamArray;
@property (nonatomic,strong) __block NSArray *yourTeamArray;

-(IBAction)refreshData:id;
-(IBAction)settingsButtonPressed:id;
-(void)initButtons;

@end

@implementation VC_Upcoming

- (id)initWithStyle:(UITableViewStyle)style {
    self = [super initWithStyle:style];
    if (self) {}
    return self;
}

-(void)viewDidLoad {
    [super viewDidLoad];
    [self initButtons];
    
    _myManager = [[MemoryManager alloc] init];
    _gameArray = [_myManager getGameArray];
    
    if(_gameArray.count <= 0) {
        [_myManager refreshData];
        _gameArray = [_myManager getGameArray];
    }
    
    _teamArray = [_myManager getTeamArray];
    _yourTeamArray = [_myManager getYourTeamArray];
    _gameArray = [self trimGameArray];
    
    self.tableView.backgroundView = nil;
    self.view.backgroundColor = [UIColor colorWithRed:0.2f green:0.2f blue:0.2f alpha:1.0f];
    [self.tableView reloadData];
}

-(NSArray *)trimGameArray {
    NSMutableArray *gameMutable = [[NSMutableArray alloc] initWithArray:_gameArray];
    for(int i=gameMutable.count-1;i>=0;i--)
        if(![self isYourGame:[gameMutable objectAtIndex:i]])
            [gameMutable removeObjectAtIndex:i];
    
        return [gameMutable sortedArrayUsingComparator:^NSComparisonResult(Game *a, Game *b) {
           NSDate *first = [a getDateObject];
           NSDate *second = [b getDateObject];
           return [first compare:second];
       }];
}
                 
-(BOOL)isYourGame:(Game *)gameToTest {
    for(int i=0;i<_yourTeamArray.count;i++) {
        Team *teamAtIndex = [_yourTeamArray objectAtIndex:i];
        if([gameToTest.homeTeam isEqualToString:teamAtIndex.teamName] && [gameToTest.league isEqualToString:teamAtIndex.league])
            return YES;
        if([gameToTest.awayTeam isEqualToString:teamAtIndex.teamName] && [gameToTest.league isEqualToString:teamAtIndex.league])
            return YES;
    }
    return NO;
}

-(IBAction)refreshData:(id)sender {
    _myManager = [[MemoryManager alloc]init];
    [_myManager refreshData];
    
    _gameArray = [_myManager getGameArray];
    _teamArray = [_myManager getTeamArray];
    _yourTeamArray = [_myManager getYourTeamArray];
    
    _gameArray = [self trimGameArray];
    [self.tableView reloadData];
}

-(IBAction)settingsButtonPressed:(id)sender {
    UIViewController *vc_Settings = [[self storyboard] instantiateViewControllerWithIdentifier:@"id_vc_settings"];
    vc_Settings.modalTransitionStyle = UIModalTransitionStyleFlipHorizontal;
    [self presentViewController:vc_Settings animated:YES completion:nil];
}

-(void)initButtons {
    UIBarButtonItem *settings = [[UIBarButtonItem alloc] initWithImage:[UIImage imageNamed:@"gear.png"] style:UIBarButtonItemStylePlain target:self action:@selector(settingsButtonPressed:)];
    UIBarButtonItem *refresh = [[UIBarButtonItem alloc] initWithImage:[UIImage imageNamed:@"reload.png"] style:UIBarButtonItemStylePlain target:self action:@selector(refreshData:)];
    self.navigationItem.rightBarButtonItem = settings;
    self.navigationItem.leftBarButtonItem = refresh;
}

#pragma mark - Table view data source

-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    if(_yourTeamArray.count > 0)
        return _gameArray.count;
    else
        return 1;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    if(_yourTeamArray.count > 0) {
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"id_cell_upcomingGames" forIndexPath:indexPath];
    
        if (cell == nil)
            cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"id_cell_upcomingGames"];
    
        Game *temp = ((Game *)[_gameArray objectAtIndex:indexPath.row]);
        
        ((UILabel *)[cell viewWithTag:1]).text = temp.homeTeam;
        ((UILabel *)[cell viewWithTag:2]).text = temp.awayTeam;
        ((UILabel *)[cell viewWithTag:3]).text = [NSString stringWithFormat:@"%@ on %@, at %@",temp.day,temp.date,temp.startTime];
        ((UILabel *)[cell viewWithTag:4]).text = [temp.league stringByAppendingString:@" League"];
        ((UILabel *)[cell viewWithTag:5]).text = [temp.rink stringByAppendingString:@" Rink"];

        return cell;
    }
    else {
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"id_cell_upcomingGamesAddTeam" forIndexPath:indexPath];
        if (cell == nil)
            cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"id_cell_upcomingGamesAddTeam"];
        return cell;
    }
}

@end